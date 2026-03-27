package trung.supper.englishgrammar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import trung.supper.englishgrammar.enums.Level;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicResponse {
    private UUID id;
    private String name;
    private String slug;
    private String description;
    private Level level;
    private Boolean isPublished;
    private String categoryName;
}
