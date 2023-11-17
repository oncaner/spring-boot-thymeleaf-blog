package caner.blog.service;

import caner.blog.model.User;
import caner.blog.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {

    String validateToken(String token);

    void saveVerificationTokenForUser(User user, String token);

    Optional<VerificationToken> findByToken(String token);

    void deleteVerificationTokensByUserId(Long id);
}
