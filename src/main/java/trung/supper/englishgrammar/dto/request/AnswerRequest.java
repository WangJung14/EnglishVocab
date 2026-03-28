package trung.supper.englishgrammar.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {

    @NotNull(message = "Question ID is required")
    private UUID questionId;

    private List<UUID> selectedOptionIds;

    private String textAnswer;
}
