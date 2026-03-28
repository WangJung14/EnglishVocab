package trung.supper.englishgrammar.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddWordToLessonRequest {
    @NotNull(message = "Word ID is securely required!")
    private UUID wordId;
}
