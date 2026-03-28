package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.LessonWordResponse;
import trung.supper.englishgrammar.services.ILessonWordService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonWordPublicController {

    private final ILessonWordService lessonWordService;

    @GetMapping("/{lessonId}/words")
    public ApiRespone<List<LessonWordResponse>> getLessonWords(@PathVariable UUID lessonId) {
        return ApiRespone.<List<LessonWordResponse>>builder()
                .result(lessonWordService.getWordsForLesson(lessonId))
                .code(1000)
                .message("success")
                .build();
    }
}
