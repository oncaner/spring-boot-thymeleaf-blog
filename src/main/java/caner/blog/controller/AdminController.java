package caner.blog.controller;

import caner.blog.exception.AdminCannotBeLockedException;
import caner.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @GetMapping()
    public String getAdmin() {
        return "admin";
    }

    @GetMapping("/user-list")
    public String getAdminUserList(Model model) {

        model.addAttribute("users", userService.getAllUsers());

        return "admin-user-list";
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