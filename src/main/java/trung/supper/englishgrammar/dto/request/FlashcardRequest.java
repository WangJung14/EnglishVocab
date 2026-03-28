package trung.supper.englishgrammar.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlashcardRequest {

    @NotBlank(message = "Front text is required")
    private String frontText;

    @NotBlank(message = "Back text is required")
    private String backText;

    private String example;
}
