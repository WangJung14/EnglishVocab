package trung.supper.englishgrammar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.AddWordToLessonRequest;
import trung.supper.englishgrammar.dto.request.ReorderLessonWordsRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.LessonWordResponse;
import trung.supper.englishgrammar.services.ILessonWordService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/teacher/lessons")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class LessonWordTeacherController {

    private final ILessonWordService lessonWordService;

    @PostMapping("/{lessonId}/words")
    public ApiRespone<LessonWordResponse> addWordToLesson(
            @PathVariable UUID lessonId, 
            @Valid @RequestBody AddWordToLessonRequest request) {
        return ApiRespone.<LessonWordResponse>builder()
                .result(lessonWordService.addWordToLesson(lessonId, request))
                .code(1000)
                .message("Successfully added word to lesson")
                .build();
    }

    @DeleteMapping("/{lessonId}/words/{wordId}")
    public ApiRespone<Void> removeWordFromLesson(
            @PathVariable UUID lessonId, 
            @PathVariable UUID wordId) {
        lessonWordService.removeWordFromLesson(lessonId, wordId);
        return ApiRespone.<Void>builder()
                .code(1000)
                .message("Successfully removed word from lesson")
                .build();
    }

    @PutMapping("/{lessonId}/words/reorder")
    public ApiRespone<List<LessonWordResponse>> reorderWords(
            @PathVariable UUID lessonId,
            @Valid @RequestBody ReorderLessonWordsRequest request) {
        return ApiRespone.<List<LessonWordResponse>>builder()
                .result(lessonWordService.reorderWords(lessonId, request))
                .code(1000)
                .message("Successfully reordered words")
                .build();
    }
}
