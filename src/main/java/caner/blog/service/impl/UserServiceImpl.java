package caner.blog.service.impl;

import caner.blog.dto.request.RegistrationRequest;
import caner.blog.dto.response.UserDTO;
import caner.blog.model.User;
import caner.blog.repository.UserRepository;
import caner.blog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
    public UserDTO registerUser(RegistrationRequest request) {
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        return UserDTO.builder()
                .id(savedUser.getId())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .nickname(savedUser.getNickname())
                .email(savedUser.getEmail())
                .build();
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email);

        return UserDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }
}
