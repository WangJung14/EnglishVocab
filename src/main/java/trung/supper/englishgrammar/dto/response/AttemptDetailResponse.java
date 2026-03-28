package trung.supper.englishgrammar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import trung.supper.englishgrammar.enums.AttemptStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttemptDetailResponse {
    private UUID id;
    private Double score;
    private Double maxScore;
    private Boolean isPassed;
    private AttemptStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime submittedAt;
    private List<AnswerDetailResponse> answers;
}
