package trung.supper.englishgrammar.services;

import org.springframework.web.multipart.MultipartFile;
import trung.supper.englishgrammar.dto.response.BulkImportResponse;

public interface IWordImportService {
    BulkImportResponse importWords(MultipartFile file);
    byte[] generateTemplate();
}
