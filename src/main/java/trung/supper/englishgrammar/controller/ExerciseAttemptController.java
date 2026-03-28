package trung.supper.englishgrammar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.SubmitAttemptRequest;
import trung.supper.englishgrammar.dto.response.*;
import trung.supper.englishgrammar.sercurity.CustomUserDetails;
import trung.supper.englishgrammar.services.IExerciseAttemptService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
public class ExerciseAttemptController {

    private final IExerciseAttemptService attemptService;

    @PostMapping("/{exerciseId}/start")
    public ApiRespone<StartAttemptResponse> startAttempt(
            @PathVariable UUID exerciseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<StartAttemptResponse>builder()
                .result(attemptService.startAttempt(exerciseId, userDetails.getUser().getId()))
                .code(1000)
                .message("Attempt started")
                .build();
    }

    @PostMapping("/{exerciseId}/submit")
    public ApiRespone<AttemptResponse> submitAttempt(
            @PathVariable UUID exerciseId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody SubmitAttemptRequest request) {
        return ApiRespone.<AttemptResponse>builder()
                .result(attemptService.submitAttempt(exerciseId, userDetails.getUser().getId(), request))
                .code(1000)
                .message("Attempt submitted")
                .build();
    }

    @GetMapping("/{exerciseId}/attempts")
    public ApiRespone<List<AttemptResponse>> getMyAttempts(
            @PathVariable UUID exerciseId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<List<AttemptResponse>>builder()
                .result(attemptService.getMyAttempts(exerciseId, userDetails.getUser().getId()))
                .code(1000)
                .message("success")
                .build();
    }
}
