package caner.blog.controller;

import caner.blog.common.mapper.ModelMapperService;
import caner.blog.dto.response.PostDTO;
import caner.blog.dto.response.UserDTO;
import caner.blog.exception.ImageExtensionException;
import caner.blog.model.User;
import caner.blog.service.PostService;
import caner.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final ModelMapperService modelMapperService;

    @GetMapping()
    public String getUserById(Model model, Principal principal) {

        String userEmail = principal.getName();

        User user = userService.findByEmail(userEmail).get();

        UserDTO userDTO = modelMapperService.forResponse().map(user, UserDTO.class);

        if (user.getImagePath() == null || user.getImagePath().isEmpty()) {
            user.setImagePath("default-user-image.png");
        }

        model.addAttribute("user", userDTO);
        model.addAttribute("principalEmail", userEmail);

        return "user";
    }

    @GetMapping("/{id}/profile")
    public String getUserByIdForProfile(@PathVariable("id") Long id, Model model, Principal principal) {
        String principalEmail = principal.getName();

        User user = userService.findUserById(id).get();

        List<PostDTO> postDTOList = postService.getAllPostsByUserId(id);

        UserDTO userDTO = modelMapperService.forResponse().map(user, UserDTO.class);

        if (user.getImagePath() == null || user.getImagePath().isEmpty()) {
            userDTO.setImagePath("default-user-image.png");
        }

        model.addAttribute("user", userDTO);
        model.addAttribute("principalEmail", principalEmail);
        model.addAttribute("posts", postDTOList);

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

    //Admin paneline eklenecek.

//    @GetMapping("/delete/{id}")
//    public String deleteUser(@PathVariable("id") Long id) {
//        userService.deleteUserById(id);
//
//        return "redirect:/users?delete_success";
//    }

    @PostMapping("/{id}/image")
    public String uploadUserProfileImage(@PathVariable("id") Long id,
                                         @RequestParam("image") MultipartFile file,
                                         RedirectAttributes redirectAttributes
    ) {

        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("emptyFile", "Lütfen bir resim seçiniz.");
            return "redirect:/user";
        }

        try {
            userService.uploadUserProfileImage(file, id);

            return "redirect:/user";
        } catch (Exception exception) {
            if (exception.getClass().equals(ImageExtensionException.class)) {
                redirectAttributes.addFlashAttribute("imageExtensionErrorMessage", exception.getMessage());
            } else {
                redirectAttributes.addFlashAttribute("unknownImageError", "Resim yüklenirken hata oluştu.");
            }
            return "redirect:/user";
        }
    }

}
