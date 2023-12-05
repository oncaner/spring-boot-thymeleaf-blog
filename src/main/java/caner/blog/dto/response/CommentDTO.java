package caner.blog.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {

    private Long id;
    private String comment;
    private LocalDateTime createdDate;
    private PostDTO postDTO;
    private UserDTO userDTO;


}
