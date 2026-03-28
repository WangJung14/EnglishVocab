package trung.supper.englishgrammar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OptionRequest {

    @NotBlank(message = "Option content is required")
    private String content;

    @NotNull(message = "isCorrect flag is required")
    private Boolean isCorrect;
}
