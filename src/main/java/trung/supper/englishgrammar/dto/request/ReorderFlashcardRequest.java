package trung.supper.englishgrammar.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReorderFlashcardRequest {
    @NotEmpty(message = "Card IDs list cannot be empty")
    private List<UUID> cardIds;
}
