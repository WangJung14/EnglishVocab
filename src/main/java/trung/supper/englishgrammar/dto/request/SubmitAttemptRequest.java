package trung.supper.englishgrammar.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAttemptRequest {

    @NotEmpty(message = "Answers list cannot be empty")
    @Valid
    private List<AnswerRequest> answers;
}
