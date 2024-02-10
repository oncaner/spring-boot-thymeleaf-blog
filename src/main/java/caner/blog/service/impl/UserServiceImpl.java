package caner.blog.service.impl;

import caner.blog.common.mapper.ModelMapperService;
import caner.blog.common.util.ImageValidatorUtil;
import caner.blog.common.util.UserRegistrationHelper;
import caner.blog.dto.request.RegistrationRequest;
import caner.blog.dto.response.UserDTO;
import caner.blog.enums.Role;
import caner.blog.exception.AdminCannotBeLockedException;
import caner.blog.exception.FirstAndLastNameException;
import caner.blog.exception.NicknameSizeException;
import caner.blog.exception.UserNotFoundException;
import caner.blog.model.User;
import caner.blog.repository.UserRepository;
import caner.blog.service.PasswordResetTokenService;
import caner.blog.service.UserService;
import caner.blog.service.VerificationTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
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
    private final ModelMapperService modelMapperService;

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> userList = userRepository.findAll();

        return userList.stream()
                .map(user -> modelMapperService.forResponse()
                        .map(user, UserDTO.class)).toList();
    }

    @Override
    public Page<User> getAllPageableUsers(String page, int size) {

        try {
            int pageNumber = Integer.parseInt(page);

            if (pageNumber < 1) {
                pageNumber = 1;
            }

            Pageable pageable = PageRequest.of(pageNumber - 1, size);

            return userRepository.findAll(pageable);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Sayfa numarası 0'dan büyük olmalı veya sayı olmalıdır.");
        }

    }

    @Override
    public Page<User> searchUsersByUsername(String username, String page, int size) {
        try {
            int pageNumber = Integer.parseInt(page);

            if (pageNumber < 1) {
                pageNumber = 1;
            }

            Pageable pageable = PageRequest.of(pageNumber - 1, size);

            return userRepository.findByNicknameContainingIgnoreCase(username, pageable);

        } catch (NumberFormatException e) {
            throw new NumberFormatException("Sayfa numarası 0'dan büyük olmalı veya sayı olmalıdır.");
        }
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
                .isAccountNonLocked(true)
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

        if (request.getNickname().isEmpty() || request.getNickname().isBlank() ||
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
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + id));

        user.setImagePath(fileName);
        userRepository.save(user);
    }

    @Override
    public void lockUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı." + id));

        if (user.getRole().equals(Role.ROLE_ADMIN)) {
            throw new AdminCannotBeLockedException("Admin hesabı kilitlenemez.");
        }

        if (user.isAccountNonLocked()) {
            user.setAccountNonLocked(false);
            userRepository.save(user);
        }
    }

    @Override
    public void unlockUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı." + id));

        if (!user.isAccountNonLocked()) {
            user.setAccountNonLocked(true);
            userRepository.save(user);
        }

    }

}
