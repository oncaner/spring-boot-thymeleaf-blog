package caner.blog.controller;

import caner.blog.dto.request.RegistrationRequest;
import caner.blog.event.RegistrationCompleteEvent;
import caner.blog.event.ResetPasswordEvent;
import caner.blog.event.listener.RegistrationCompleteEventListener;
import caner.blog.event.listener.ResetPasswordEventListener;
import caner.blog.model.User;
import caner.blog.model.VerificationToken;
import caner.blog.service.PasswordResetTokenService;
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
import java.util.UUID;

import static caner.blog.utils.Constant.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VerificationTokenService verificationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final RegistrationCompleteEventListener registrationCompleteEventListener;
    private final ResetPasswordEventListener resetPasswordEventListener;

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
            case EXPIRED:
                return "redirect:/error?expired";
            case VALID:
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }

    @GetMapping("/forgot-password-request")
    public String forgotPasswordForm() {

        return "forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String resetPasswordRequest(HttpServletRequest request) {

        String email = request.getParameter("email");
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "redirect:/registration/forgot-password-request?not_found";
        }

        User user = optionalUser.get();

        String url = UrlUtil.getApplicationUrl(request);

        applicationEventPublisher.publishEvent(new ResetPasswordEvent(user, url));

        return "redirect:/registration/forgot-password-request?success";
    }

    @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token, Model model) {

        model.addAttribute("token", token);

        return "password-reset-form";
    }

    @PostMapping("/reset-password")
    public String resetPassword(HttpServletRequest request) {

        String token = request.getParameter("token");
        String password = request.getParameter("password");

        String tokenVerificationResult = passwordResetTokenService.validatePasswordResetToken(token);

        if (!tokenVerificationResult.equals(VALID)) {
            return "redirect:/error?invalid_token";
        }

        Optional<User> optionalUser = passwordResetTokenService.findUserByPasswordResetToken(token);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();
            passwordResetTokenService.resetPassword(user, password);

            return "redirect:/login?reset_success";
        }

        return "redirect:/error?not_found";

    }
}
