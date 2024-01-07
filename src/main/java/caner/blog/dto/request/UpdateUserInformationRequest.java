package caner.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserInformationRequest {

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

    @NotNull(message = "Bu alanı doldurunuz!")
    @NotBlank(message = "Boş geçilemez!")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Geçersiz e-posta adresi")
    private String email;
}
