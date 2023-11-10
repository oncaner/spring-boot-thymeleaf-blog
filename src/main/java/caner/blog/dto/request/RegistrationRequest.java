package caner.blog.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequest {

    private String firstName;
    private String lastName;
    private String nickname;
    private String email;
    private String password;
    private String confirmPassword;

}
