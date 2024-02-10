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
public class PostDTO {

    private Long id;
    private String title;
    private String shortInformation;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private UserDTO userDTO;
}
