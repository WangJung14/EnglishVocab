package trung.supper.englishgrammar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.ReadingRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.ReadingResponse;
import trung.supper.englishgrammar.services.IReadingPassageService;

import java.util.UUID;

@RestController
@RequestMapping("/teacher/lessons")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class ReadingPassageTeacherController {

    private final IReadingPassageService readingPassageService;

    @PostMapping("/{lessonId}/reading")
    public ApiRespone<ReadingResponse> createReadingPassage(
            @PathVariable UUID lessonId,
            @Valid @RequestBody ReadingRequest request) {
        return ApiRespone.<ReadingResponse>builder()
                .result(readingPassageService.createReadingPassage(lessonId, request))
                .code(1000)
                .message("Successfully created reading passage")
                .build();
    }

    @PutMapping("/{lessonId}/reading")
    public ApiRespone<ReadingResponse> updateReadingPassage(
            @PathVariable UUID lessonId,
            @Valid @RequestBody ReadingRequest request) {
        return ApiRespone.<ReadingResponse>builder()
                .result(readingPassageService.updateReadingPassage(lessonId, request))
                .code(1000)
                .message("Successfully updated reading passage")
                .build();
    }

    @DeleteMapping("/{lessonId}/reading")
    public ApiRespone<Void> deleteReadingPassage(@PathVariable UUID lessonId) {
        readingPassageService.deleteReadingPassage(lessonId);
        return ApiRespone.<Void>builder()
                .code(1000)
                .message("Successfully deleted reading passage")
                .build();
    }
}
