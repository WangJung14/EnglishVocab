package trung.supper.englishgrammar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import trung.supper.englishgrammar.enums.CefrLevel;
import trung.supper.englishgrammar.enums.PartOfSpeech;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WordResponse {
    private UUID id;
    private String word;
    private String meaning;
    private String pronunciation;
    private String example;
    private CefrLevel level;
    private PartOfSpeech partOfSpeech;
}
