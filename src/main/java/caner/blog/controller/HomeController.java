package caner.blog.controller;

import lombok.RequiredArgsConstructor;
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
    public String login() {

        return "login";
    }

    @GetMapping("/error")
    public String error() {

        return "error";
    }

}
