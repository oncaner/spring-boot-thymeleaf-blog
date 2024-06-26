package caner.blog.event.listener;

import caner.blog.event.RegistrationCompleteEvent;
import caner.blog.model.User;
import caner.blog.service.VerificationTokenService;
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
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final VerificationTokenService verificationTokenService;
    private final JavaMailSenderImpl javaMailSender;
    private User user;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        //User'ı al.
        user = event.getUser();

        //User için bir token üret.
        String verificationToken = UUID.randomUUID().toString();

        //User için token'i kaydet.
        verificationTokenService.saveVerificationTokenForUser(user, verificationToken);

        //Verification url oluştur
        String confirmationUrl = event.getConfirmationUrl() + "/registration/verifyEmail?token=" + verificationToken;

        //User için mail yolla.
        try {
            sendVerificationEmail(confirmationUrl);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {

        String subject = "Email Verification";
        String senderName = "User Registration Portal Service";
        String mailContent = "<p> Hi, " + user.getFirstName() + ", </p>" +
                "<p>Thank you for registering with us," + "" +
                "Please, follow the link below to complete your registration.</p>" +
                "<a href=\"" + url + "\">Verify your email to activate your account</a>" +
                "<p> Thank you <br> Users Registration Portal Service";

        UserEmailUtil.emailMessage(subject, senderName, mailContent, javaMailSender, user);
    }
}
