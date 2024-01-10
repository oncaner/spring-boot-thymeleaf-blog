package caner.blog.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotNull(message = "Bu alanı doldurunuz!")
    @NotBlank(message = "Boş geçilemez!")
    private String firstName;

    @NotNull(message = "Bu alanı doldurunuz!")
    @NotBlank(message = "Boş geçilemez!")
    private String lastName;

    @NotNull(message = "Bu alanı doldurunuz!")
    @NotBlank(message = "Boş geçilemez!")
    @Size(min = 5, max = 25, message = "Nickname 5 ile 25 arasında olmak zorunda!")
    private String nickname;

    private String imagePath;

//    @NotNull(message = "Bu alanı doldurunuz!")
//    @NotBlank(message = "Boş geçilemez!")
//    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Geçersiz e-posta adresi")
    private String email;

    private String role;

    private boolean isEnabled;

    private boolean isAccountNonLocked;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
