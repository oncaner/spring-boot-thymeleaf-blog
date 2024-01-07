package caner.blog.service;

import caner.blog.dto.request.RegistrationRequest;
import caner.blog.dto.request.UpdateUserInformationRequest;
import caner.blog.dto.response.UserDTO;
import caner.blog.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> getAllUsers();

    User registerUser(RegistrationRequest registrationRequest);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findUserById(Long id);

    void updateUser(UserDTO request, Principal principal);

    void deleteUserById(Long id);

    void uploadUserProfileImage(MultipartFile file, Long id);
}
