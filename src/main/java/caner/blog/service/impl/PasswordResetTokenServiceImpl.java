package caner.blog.service.impl;

import caner.blog.dto.request.PasswordResetRequest;
import caner.blog.model.PasswordResetToken;
import caner.blog.model.User;
import caner.blog.repository.PasswordResetTokenRepository;
import caner.blog.repository.UserRepository;
import caner.blog.service.PasswordResetTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static caner.blog.common.Constant.*;

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
    public String resetPassword(User user, PasswordResetRequest request) {

        if (request.getNewPassword().equals(request.getConfirmPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);

            return SUCCESS;
        }

        return FAIL;

    }

    @Override
    public void deletePasswordResetTokensByUserId(Long id) {
        List<PasswordResetToken> passwordResetTokenList = passwordResetTokenRepository.findAllByUserId(id);

        if(!passwordResetTokenList.isEmpty()){
            passwordResetTokenRepository.deleteAll(passwordResetTokenList);
        }
    }
}
