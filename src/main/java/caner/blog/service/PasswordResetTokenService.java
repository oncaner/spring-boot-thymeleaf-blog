package caner.blog.service;

import caner.blog.dto.request.PasswordResetRequest;
import caner.blog.model.PasswordResetToken;
import caner.blog.model.User;

import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenService {
    void createPasswordResetTokenForUser(User user, String passwordResetToken);

    String validatePasswordResetToken(String token);

    Optional<User> findUserByPasswordResetToken(String token);

    String resetPassword(User user, PasswordResetRequest passwordResetRequest);

    void deletePasswordResetTokensByUserId(Long id);
}
