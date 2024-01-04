package caner.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePostRequest {

    @NotNull(message = "Bu alanı doldurunuz!")
    @NotBlank(message = "Boş geçilemez!")
    private String title;

    @NotNull(message = "Bu alanı doldurunuz!")
    @NotBlank(message = "Boş geçilemez!")
    private String content;
}
