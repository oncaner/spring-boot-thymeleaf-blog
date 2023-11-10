package caner.blog.service;

import caner.blog.dto.request.RegistrationRequest;
import caner.blog.dto.response.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO registerUser(RegistrationRequest registrationRequest);

    UserDTO findByEmail(String email);

}
