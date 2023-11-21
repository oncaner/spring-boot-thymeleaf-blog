package caner.blog.controller;

import caner.blog.common.util.UrlUtil;
import caner.blog.dto.request.PasswordResetRequest;
import caner.blog.dto.request.RegistrationRequest;
import caner.blog.event.RegistrationCompleteEvent;
import caner.blog.event.ResetPasswordEvent;
import caner.blog.model.User;
import caner.blog.model.VerificationToken;
import caner.blog.service.PasswordResetTokenService;
import caner.blog.service.UserService;
import caner.blog.service.VerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static caner.blog.common.Constant.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final VerificationTokenService verificationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;

    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationRequest());

        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid RegistrationRequest registrationRequest,
                               BindingResult result, Model model, HttpServletRequest request) {

        User user = null;

        if (result.hasErrors()) {
            return "registration";
        }

        try {
            user = userService.registerUser(registrationRequest);

            applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(user, UrlUtil.getApplicationUrl(request)));
        } catch (Exception exception) {
            if (exception.getClass().equals(MailSendException.class)) {
                model.addAttribute("mailErrorMessage",
                        "The email verification link could not be sent." +
                                " Please check your internet speed and register again.");

                verificationTokenService.deleteVerificationTokensByUserId(user.getId());
                userService.deleteUserById(user.getId());
            } else {
                model.addAttribute("errorMessage", exception.getMessage());
            }

            return "registration";
        }

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
                return "redirect:/registration/registration-form?expired";
            case VALID:
                return "redirect:/login?valid";
            default:
                return "redirect:/registration/registration-form?invalid";
        }
    }

    @GetMapping("/forgot-password-request")
    public String forgotPasswordForm() {

        return "forgot-password-form";
    }

    @PostMapping("/forgot-password")
    public String resetPasswordRequest(Model model, HttpServletRequest request) {

        String email = request.getParameter("email");
        Optional<User> optionalUser = userService.findByEmail(email);

        if (optionalUser.isEmpty()) {
            return "redirect:/registration/forgot-password-request?not_found";
        }

        User user = optionalUser.get();

        String url = UrlUtil.getApplicationUrl(request);

        try {
            applicationEventPublisher.publishEvent(new ResetPasswordEvent(user, url));
        } catch (Exception exception) {
            if (exception.getClass().equals(MailSendException.class)) {
                model.addAttribute("passwordResetMailError",
                        "The email password-reset link could not be sent." +
                                " Please check your internet speed and send again.");

                passwordResetTokenService.deletePasswordResetTokensByUserId(user.getId());

                return "forgot-password-form";
            }
        }

        return "redirect:/registration/forgot-password-request?success";
    }

    @GetMapping("/password-reset-form")
    public String passwordResetForm(@RequestParam("token") String token, Model model) {

        model.addAttribute("token", token);
        model.addAttribute("passwordResetRequest", new PasswordResetRequest());

        return "password-reset-form";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@ModelAttribute("passwordResetRequest") @Valid PasswordResetRequest passwordResetRequest,
                                BindingResult bindingResult, HttpServletRequest request) {

        String token = request.getParameter("token");

        if (bindingResult.hasErrors()) {
            return "password-reset-form";
        }

        String tokenVerificationResult = passwordResetTokenService.validatePasswordResetToken(token);

        if (!tokenVerificationResult.equals(VALID)) {
            return "redirect:/registration/forgot-password-request?invalid_token";
        }

        Optional<User> optionalUser = passwordResetTokenService.findUserByPasswordResetToken(token);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();
            String result = passwordResetTokenService.resetPassword(user, passwordResetRequest);

            if (result.equals(SUCCESS)) {
                return "redirect:/login?reset_success";
            } else {
                return "redirect:/registration/password-reset-form?fail&token=" + token;
            }
        }

        return "redirect:/registration/password-reset-form?not_found&token=" + token;
    }
}
