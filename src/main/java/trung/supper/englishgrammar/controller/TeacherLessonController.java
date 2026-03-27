package trung.supper.englishgrammar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.LessonReorderRequest;
import trung.supper.englishgrammar.dto.request.LessonRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.LessonResponse;
import trung.supper.englishgrammar.services.ILessonService;

import java.util.UUID;

@RestController
@RequestMapping("/teacher/lessons")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class TeacherLessonController {

    private final ILessonService lessonService;

    @PostMapping
    public ApiRespone<LessonResponse> createLesson(@Valid @RequestBody LessonRequest request) {
        return ApiRespone.<LessonResponse>builder()
                .result(lessonService.createLesson(request))
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/{id}")
    public ApiRespone<LessonResponse> updateLesson(@PathVariable UUID id, @Valid @RequestBody LessonRequest request) {
        return ApiRespone.<LessonResponse>builder()
                .result(lessonService.updateLesson(id, request))
                .code(1000)
                .message("success")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiRespone<String> deleteLesson(@PathVariable UUID id) {
        lessonService.deleteLesson(id);
        return ApiRespone.<String>builder()
                .result("Deleted successfully")
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/{id}/publish")
    public ApiRespone<LessonResponse> togglePublish(@PathVariable UUID id) {
        return ApiRespone.<LessonResponse>builder()
                .result(lessonService.togglePublish(id))
                .code(1000)
                .message("success")
                .build();
    }

    @PutMapping("/reorder")
    public ApiRespone<String> reorderLessons(@RequestBody LessonReorderRequest request) {
        lessonService.reorderLessons(request);
        return ApiRespone.<String>builder()
                .result("Reordered successfully")
                .code(1000)
                .message("success")
                .build();
    }
}
