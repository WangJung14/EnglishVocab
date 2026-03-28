package trung.supper.englishgrammar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.UpdateProgressRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.LessonProgressResponse;
import trung.supper.englishgrammar.dto.response.TopicProgressResponse;
import trung.supper.englishgrammar.sercurity.CustomUserDetails;
import trung.supper.englishgrammar.services.ILessonProgressService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LessonProgressController {

    private final ILessonProgressService progressService;

    @GetMapping("/users/me/progress")
    public ApiRespone<List<LessonProgressResponse>> getAllProgress(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<List<LessonProgressResponse>>builder()
                .result(progressService.getAllProgress(userDetails.getUser().getId()))
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/users/me/progress/topics/{topicId}")
    public ApiRespone<TopicProgressResponse> getTopicProgress(
            @PathVariable UUID topicId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<TopicProgressResponse>builder()
                .result(progressService.getTopicProgress(userDetails.getUser().getId(), topicId))
                .code(1000)
                .message("success")
                .build();
    }

    @PostMapping("/lessons/{lessonId}/progress/start")
    public ApiRespone<LessonProgressResponse> startLesson(
            @PathVariable UUID lessonId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<LessonProgressResponse>builder()
                .result(progressService.startLesson(userDetails.getUser().getId(), lessonId))
                .code(1000)
                .message("Lesson started")
                .build();
    }

    @PutMapping("/lessons/{lessonId}/progress")
    public ApiRespone<LessonProgressResponse> updateProgress(
            @PathVariable UUID lessonId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateProgressRequest request) {
        return ApiRespone.<LessonProgressResponse>builder()
                .result(progressService.updateProgress(userDetails.getUser().getId(), lessonId, request))
                .code(1000)
                .message("Progress updated")
                .build();
    }

    @PostMapping("/lessons/{lessonId}/progress/complete")
    public ApiRespone<LessonProgressResponse> completeLesson(
            @PathVariable UUID lessonId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<LessonProgressResponse>builder()
                .result(progressService.completeLesson(userDetails.getUser().getId(), lessonId))
                .code(1000)
                .message("Lesson completed")
                .build();
    }
}
