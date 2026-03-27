package trung.supper.englishgrammar.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkImportResponse {
    private int total;
    private int success;
    private int failed;
    private List<String> errors;
}
