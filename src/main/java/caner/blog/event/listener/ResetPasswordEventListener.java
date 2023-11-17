package caner.blog.event.listener;

import caner.blog.event.ResetPasswordEvent;
import caner.blog.model.User;
import caner.blog.service.PasswordResetTokenService;
import caner.blog.common.util.UserEmailUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ResetPasswordEventListener implements ApplicationListener<ResetPasswordEvent> {

    private final PasswordResetTokenService passwordResetTokenService;
    private final JavaMailSenderImpl javaMailSender;
    private User user;

    @Override
    public void onApplicationEvent(ResetPasswordEvent event) {

        user = event.getUser();

        String passwordResetVerificationToken = UUID.randomUUID().toString();

        passwordResetTokenService.createPasswordResetTokenForUser(user, passwordResetVerificationToken);

        String confirmationUrl = event.getConfirmationUrl() + "/registration/password-reset-form?token=" + passwordResetVerificationToken;

        try {
            sendPasswordResetVerificationEmail(confirmationUrl);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPasswordResetVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {

        String subject = "Password Reset Request Verification";
        String senderName = "User Verification Service";
        String mailContent = "<a href=\"" + url + "\">Reset Password</a>";
        UserEmailUtil.emailMessage(subject, senderName, mailContent, javaMailSender, user);
    }
}
