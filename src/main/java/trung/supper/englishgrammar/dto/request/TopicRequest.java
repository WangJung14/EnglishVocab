package trung.supper.englishgrammar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TopicRequest {
    @NotBlank(message = "Topic name is required")
    private String name;

    private String description;

    @NotNull(message = "Level is required")
    private Level level;

    @NotNull(message = "Category is required")
    private UUID categoryId;
}
