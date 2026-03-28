package trung.supper.englishgrammar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatsResponse {
    private long totalLessonsStarted;
    private long totalLessonsCompleted;
    private double completionRate;
    private Double averageScore;
    private long totalWordsLearned;
    private long reviewCount;
}
