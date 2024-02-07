package caner.blog.controller;

import caner.blog.common.mapper.ModelMapperService;
import caner.blog.dto.response.UserDTO;
import caner.blog.exception.AdminCannotBeLockedException;
import caner.blog.model.User;
import caner.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ModelMapperService modelMapperService;

    @GetMapping()
    public String getAdmin() {
        return "admin";
    }

    @GetMapping("/user-list")
    public String getUserList(@RequestParam(value = "page", required = false,
            defaultValue = "1") String page, Model model, RedirectAttributes redirectAttributes) {

        int size = 4;

        try {
            Page<User> pageableUsers = userService.getAllPageableUsers(page, size);

            int pageNumber = Integer.parseInt(page);

            if(pageNumber < 1){
                pageNumber = 1;
            }

            int totalPages = pageableUsers.getTotalPages();

            if(pageNumber > totalPages){
                redirectAttributes.addFlashAttribute("pageNumberException", "Sayfa numarası çok fazla!");
                return "redirect:/admin/user-list";
            }

            List<UserDTO> userList = pageableUsers.getContent().stream()
                    .map(user -> modelMapperService.forResponse().map(user, UserDTO.class)).toList();

            model.addAttribute("users", userList);
            model.addAttribute("currentPage", pageNumber);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("totalItems", pageableUsers.getTotalElements());

            return "admin-user-list";

        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("numberFormatException", e.getMessage());
            return "redirect:/admin/user-list";
        }

    }

    @GetMapping("/user-lock")
    public String lockUser(@RequestParam("userId") Long id, RedirectAttributes redirectAttributes) {

        try {
            userService.lockUser(id);
        } catch (AdminCannotBeLockedException e) {
            redirectAttributes.addFlashAttribute(
                    "adminCannotBeLockedException", e.getMessage());

            return "redirect:/admin/user-list";
        }

        return "redirect:/admin/user-list";
    }

    @GetMapping("/user-unlock")
    public String unlockUser(@RequestParam("userId") Long id) {

        userService.unlockUser(id);

        return "redirect:/admin/user-list";
    }

}