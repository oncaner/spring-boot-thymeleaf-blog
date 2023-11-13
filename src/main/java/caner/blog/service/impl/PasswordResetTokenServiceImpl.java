package caner.blog.service.impl;

import caner.blog.model.PasswordResetToken;
import caner.blog.model.User;
import caner.blog.repository.PasswordResetTokenRepository;
import caner.blog.repository.UserRepository;
import caner.blog.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

import static caner.blog.utils.Constant.*;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createPasswordResetTokenForUser(User user, String passwordResetToken) {
        PasswordResetToken newPasswordResetToken = new PasswordResetToken(passwordResetToken, user);
        passwordResetTokenRepository.save(newPasswordResetToken);
    }

    @Override
    public String validatePasswordResetToken(String token) {

        Optional<PasswordResetToken> optionalToken = passwordResetTokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            return INVALID;
        }

        Calendar calendar = Calendar.getInstance();

        if (optionalToken.get().getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            return EXPIRED;
        }

        return VALID;
    }

    @Override
    public Optional<User> findUserByPasswordResetToken(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).get().getUser());
    }

    @Override
    public void resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
