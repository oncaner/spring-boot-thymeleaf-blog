package caner.blog.common.util;

import caner.blog.exception.EmailAlreadyExistsException;
import caner.blog.exception.NicknameAlreadyExistsException;
import caner.blog.model.User;
import caner.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRegistrationHelper {

    private final UserRepository userRepository;

    public String checkEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            throw new EmailAlreadyExistsException("Bu Mail'e ait zaten bir kullanıcı var!");
        } else {
            return email;
        }
    }

    public String checkNickname(String nickname) {
        Optional<User> optionalUser = userRepository.findByNickname(nickname);

        if (optionalUser.isPresent()) {
            throw new NicknameAlreadyExistsException("Bu Nickname'e ait zaten bir kullanıcı var!");
        } else {
            return nickname;
        }
    }

}
