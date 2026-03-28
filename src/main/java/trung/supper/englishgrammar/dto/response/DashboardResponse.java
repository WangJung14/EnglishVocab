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
public class DashboardResponse {
    private long totalLessonsCompleted;
    private long totalTopicsCompleted;
    private int currentStreak;
    private int longestStreak;
}
