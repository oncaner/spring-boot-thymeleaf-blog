package caner.blog.service;

import caner.blog.model.User;

import java.util.Optional;

public interface PasswordResetTokenService {
    void createPasswordResetTokenForUser(User user, String passwordResetToken);

    String validatePasswordResetToken(String token);

    Optional<User> findUserByPasswordResetToken(String token);

    void resetPassword(User user, String password);
}
