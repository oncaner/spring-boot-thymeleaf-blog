package caner.blog.service;

import caner.blog.dto.request.RegistrationRequest;
import caner.blog.dto.response.UserDTO;
import caner.blog.model.User;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> getAllUsers();

    Page<User> getAllPageableUsers(String page, int size);

    User registerUser(RegistrationRequest registrationRequest);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findUserById(Long id);

    void updateUser(UserDTO request, Principal principal);

    void deleteUserById(Long id);

    void uploadUserProfileImage(MultipartFile file, Long id);

    void lockUser(Long id);

    void unlockUser(Long id);
}
