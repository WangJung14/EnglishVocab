package trung.supper.englishgrammar.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import trung.supper.englishgrammar.enums.CefrLevel;
import trung.supper.englishgrammar.enums.PartOfSpeech;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordRequest {
    @NotBlank(message = "Word is required")
    private String word;

    @NotBlank(message = "Meaning is required")
    private String meaning;

    private String pronunciation;

    private String example;

    @NotNull(message = "Level is required")
    private CefrLevel level;

    @NotNull(message = "Part of speech is required")
    private PartOfSpeech partOfSpeech;
}
