package caner.blog.controller;

import caner.blog.common.mapper.ModelMapperService;
import caner.blog.dto.response.UserDTO;
import caner.blog.exception.ImageExtensionException;
import caner.blog.model.User;
import caner.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapperService modelMapperService;

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());

        return "users";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") Long id, Model model) {
        UserDTO user = modelMapperService.forResponse().map(userService.findUserById(id).get(), UserDTO.class);

        model.addAttribute("user", user);

        return "user-profile";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Optional<User> optionalUser = userService.findUserById(id);
        model.addAttribute("user", optionalUser.get());

        return "update-user";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
        userService.updateUser(id, user.getFirstName(), user.getLastName(), user.getEmail());

        return "redirect:/users?update_success";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteUserById(id);

        return "redirect:/users?delete_success";
    }

    @PostMapping("/{id}/image")
    public String uploadUserProfileImage(@PathVariable("id") Long id,
                                         @RequestParam("image") MultipartFile file,
                                         RedirectAttributes redirectAttributes, Model model
    ) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("emptyFile", "Lütfen bir resim seçiniz.");
            return "redirect:/users/{id}";
        }

        try {
            userService.uploadUserProfileImage(file, id);

            return "redirect:/users/{id}";
        } catch (Exception exception) {
            if (exception.getClass().equals(ImageExtensionException.class)) {
                model.addAttribute("imageExtensionErrorMessage", exception.getMessage());
                User user = userService.findUserById(id).get();
                model.addAttribute("user", user);
            } else {
                model.addAttribute("unknownImageError", "Resim yüklenirken hata oluştu.");
            }

            return "user-profile";
        }
    }

}
