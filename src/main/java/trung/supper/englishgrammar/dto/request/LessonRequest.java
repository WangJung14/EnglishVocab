package trung.supper.englishgrammar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import trung.supper.englishgrammar.enums.LessonType;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LessonRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    @NotNull(message = "Lesson type is required")
    private LessonType lessonType;

    @NotNull(message = "Topic is required")
    private UUID topicId;
}
