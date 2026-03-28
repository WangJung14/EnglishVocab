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
public class ReadingRequest {
    
    @NotBlank(message = "Title cannot be blank")
    private String title;
    
    @NotBlank(message = "Content cannot be blank")
    private String content;
    
    private String translation;
}
