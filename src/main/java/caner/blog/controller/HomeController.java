package caner.blog.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String home() {

        return "home";
    }

    @GetMapping("/login")
    public String login(Authentication authentication) {

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }

        return "login";
    }

    @GetMapping("/error")
    public String error() {

        return "error";
    }

    @GetMapping("/access-denied")
    public String access_denied() {

        return "access-denied";
    }
}
