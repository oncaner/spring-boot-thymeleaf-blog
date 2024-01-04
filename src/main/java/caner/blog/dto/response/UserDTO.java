package caner.blog.dto.response;

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
    private String firstName;
    private String lastName;
    private String imagePath;
    private String nickname;
    private String email;
    private String role;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

}
