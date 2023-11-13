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

}
