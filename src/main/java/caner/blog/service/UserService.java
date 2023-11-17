package caner.blog.service;

import caner.blog.dto.request.RegistrationRequest;
import caner.blog.dto.response.UserDTO;
import caner.blog.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> getAllUsers();

    User registerUser(RegistrationRequest registrationRequest);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findUserById(Long id);

    void updateUser(Long id, String firstName, String lastName, String email);

    void deleteUserById(Long id);
}
