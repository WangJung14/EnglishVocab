package trung.supper.englishgrammar.controller;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final Cloudinary cloudinary;

    @GetMapping("/cloudinary-test")
    public String test() {
        return cloudinary.config.cloudName;
    }
}
