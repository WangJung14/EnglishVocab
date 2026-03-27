package trung.supper.englishgrammar.dto.response;

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
public class LessonResponse {
    private UUID id;
    private String title;
    private String slug;
    private String content;
    private LessonType lessonType;
    private Boolean isPublished;
}
