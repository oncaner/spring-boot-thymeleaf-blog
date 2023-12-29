package caner.blog.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {

    @NotNull(message = "Bu alanı doldurunuz!")
    @NotBlank(message = "Boş geçilemez!")
    @Size(min = 1, max = 750, message = "Yorumunuz maksimum 750 karakter olmak zorundadır!")
    private String comment;
}
