package trung.supper.englishgrammar.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // khong them cac file null vao api tra ve
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiRespone<T> {
    @Builder.Default
    private int code = 1000; // thanh cong
    private String message;
    private T data;
}
