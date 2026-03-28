package trung.supper.englishgrammar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDetailResponse {
    private UUID questionId;
    private String questionContent;
    private UUID selectedOptionId;
    private String selectedOptionContent;
    private String textAnswer;
    private Boolean isCorrect;
    private List<UUID> correctOptionIds;
}
