package caner.blog.service.impl;

import caner.blog.model.User;
import caner.blog.model.VerificationToken;
import caner.blog.repository.UserRepository;
import caner.blog.repository.VerificationTokenRepository;
import caner.blog.service.VerificationTokenService;
import caner.blog.common.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;

    @Override
    public String validateToken(String token) {

        Optional<VerificationToken> optionalToken = verificationTokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            return Constant.INVALID;
        }

        User user = optionalToken.get().getUser();

        Calendar calendar = Calendar.getInstance();
        if ((optionalToken.get().getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0) {
            return Constant.EXPIRED;
        }

        user.setEnabled(true);

        userRepository.save(user);

        return Constant.VALID;
    }

    @Override
    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(token, user);
        verificationTokenRepository.save(verificationToken);
    }

    @Override
    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public void deleteUserToken(Long id) {
        verificationTokenRepository.deleteByUserId(id);
    }
}
