package caner.blog.service.impl;

import caner.blog.common.util.UserRegistrationHelper;
import caner.blog.dto.request.RegistrationRequest;
import caner.blog.dto.response.UserDTO;
import caner.blog.enums.Role;
import caner.blog.model.User;
import caner.blog.repository.UserRepository;
import caner.blog.service.PasswordResetTokenService;
import caner.blog.service.UserService;
import caner.blog.service.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VerificationTokenService verificationTokenService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final UserRegistrationHelper userRegistrationHelper;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();

        userList.forEach(user -> userDTOS.add(UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build()));

        return userDTOS;
    }

    @Override
    public User registerUser(RegistrationRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .nickname(userRegistrationHelper.checkNickname(request.getNickname()))
                .email(userRegistrationHelper.checkEmail(request.getEmail()))
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByNickname(String nickname) {

        return userRepository.findByNickname(nickname);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public void updateUser(Long id, String firstName, String lastName, String email) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.get();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            passwordResetTokenService.deletePasswordResetTokensByUserId(id);
            verificationTokenService.deleteVerificationTokensByUserId(id);

            userRepository.deleteById(id);
        }
    }
}
