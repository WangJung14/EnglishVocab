package trung.supper.englishgrammar.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface ICloudinaryService {
    public Map uploadFile(MultipartFile file,String foulderName) throws IOException;
    public Map uploadVideo(MultipartFile file,String foulderName) throws IOException;
}
