package caner.blog.controller;

import caner.blog.dto.request.RegistrationRequest;
import caner.blog.event.RegistrationCompleteEvent;
import caner.blog.model.User;
import caner.blog.model.VerificationToken;
import caner.blog.service.UserService;
import caner.blog.service.VerificationTokenService;
import caner.blog.utils.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VerificationTokenService verificationTokenService;

    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationRequest());

        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registrationRequest, HttpServletRequest request) {
        User user = userService.registerUser(registrationRequest);

        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user, UrlUtil.getApplicationUrl(request)));

        return "redirect:/registration/registration-form?success";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<VerificationToken> optionalToken = verificationTokenService.findByToken(token);

        if (optionalToken.isPresent() && optionalToken.get().getUser().isEnabled()) {
            return "redirect:/login?verified";
        }

        String verificationResult = verificationTokenService.validateToken(token);

        switch (verificationResult) {
            case "EXPIRED":
                return "redirect:/error?expired";
            case "VALID":
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }

}
