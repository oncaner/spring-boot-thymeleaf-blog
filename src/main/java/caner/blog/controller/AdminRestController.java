package caner.blog.controller;

import caner.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminRestController {

    private final UserService userService;

    @GetMapping("/lock-user")
    public void lockUser(@RequestParam("userId") Long userId) {
        userService.lockUser(userId);
    }

    @GetMapping("/unlock-user")
    public void unlockUser(@RequestParam("userId") Long userId) {
        userService.unlockUser(userId);
    }

}
