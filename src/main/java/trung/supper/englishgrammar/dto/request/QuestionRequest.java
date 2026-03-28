package trung.supper.englishgrammar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import trung.supper.englishgrammar.enums.QuestionType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {

    @NotBlank(message = "Question content is required")
    private String content;

    @NotNull(message = "Question type is required")
    private QuestionType type;
}
