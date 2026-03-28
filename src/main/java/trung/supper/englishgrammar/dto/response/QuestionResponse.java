package trung.supper.englishgrammar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import trung.supper.englishgrammar.enums.QuestionType;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionResponse {
    private UUID id;
    private String content;
    private QuestionType type;
    private List<OptionResponse> options;
}
