package trung.supper.englishgrammar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import trung.supper.englishgrammar.enums.ProgressStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonProgressResponse {
    private UUID lessonId;
    private String lessonTitle;
    private ProgressStatus status;
    private Integer progressPercent;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
