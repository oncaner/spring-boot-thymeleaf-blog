package caner.blog.service.impl;

import caner.blog.common.util.ImageValidatorUtil;
import caner.blog.common.util.UserRegistrationHelper;
import caner.blog.dto.request.RegistrationRequest;
import caner.blog.dto.request.UpdateUserInformationRequest;
import caner.blog.dto.response.UserDTO;
import caner.blog.enums.Role;
import caner.blog.exception.FirstAndLastNameException;
import caner.blog.exception.NicknameSizeException;
import caner.blog.model.User;
import caner.blog.repository.UserRepository;
import caner.blog.service.PasswordResetTokenService;
import caner.blog.service.UserService;
import caner.blog.service.VerificationTokenService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
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
                .imagePath("default-user-image.png")
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
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
    public void updateUser(UserDTO request, Principal principal) {
        String email = principal.getName();

        User user = findByEmail(email).orElseThrow();

        if (request.getLastName().isEmpty() || request.getLastName().isBlank() || request.getFirstName().isEmpty() ||
                request.getFirstName().isBlank()) {
            throw new FirstAndLastNameException("İsim veya soyisim boş bırakılamaz.");
        }

        if(request.getNickname().isEmpty() || request.getNickname().isBlank() ||
                request.getNickname().length() < 3 || request.getNickname().length() > 20) {
            throw new NicknameSizeException("Username 3 ile 20 karakter arasında olmalıdır.");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setNickname(request.getNickname());

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

    @Override
    public void uploadUserProfileImage(MultipartFile file, Long id) {

        String fileName = id + "_" + StringUtils.cleanPath(file.getOriginalFilename());

        ImageValidatorUtil.imageExtensionValidator(fileName); // Try Catch ekle

        Path imagePath = Paths.get("src/main/resources/static/images/" + fileName);

        try {
            Files.copy(file.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("Resim yüklenemedi.");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı: " + id));

        user.setImagePath(fileName);
        userRepository.save(user);
    }

}
