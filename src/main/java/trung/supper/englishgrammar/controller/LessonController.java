package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.LessonResponse;
import trung.supper.englishgrammar.services.ILessonService;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final ILessonService lessonService;

    @GetMapping("/{slug}")
    public ApiRespone<LessonResponse> getLessonBySlug(@PathVariable String slug) {
        return ApiRespone.<LessonResponse>builder()
                .result(lessonService.getLessonBySlug(slug))
                .code(1000)
                .message("success")
                .build();
    }
}
