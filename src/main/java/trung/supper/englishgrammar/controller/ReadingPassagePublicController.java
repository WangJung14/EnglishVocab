package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.ReadingResponse;
import trung.supper.englishgrammar.services.IReadingPassageService;

import java.util.UUID;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class ReadingPassagePublicController {

    private final IReadingPassageService readingPassageService;

    @GetMapping("/{lessonId}/reading")
    public ApiRespone<ReadingResponse> getReadingPassage(@PathVariable UUID lessonId) {
        return ApiRespone.<ReadingResponse>builder()
                .result(readingPassageService.getReadingPassageByLessonId(lessonId))
                .code(1000)
                .message("success")
                .build();
    }
}
