package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.AttemptDetailResponse;
import trung.supper.englishgrammar.dto.response.AttemptResponse;
import trung.supper.englishgrammar.sercurity.CustomUserDetails;
import trung.supper.englishgrammar.services.IExerciseAttemptService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AttemptDetailController {

    private final IExerciseAttemptService attemptService;

    @GetMapping("/attempts/{attemptId}")
    public ApiRespone<AttemptDetailResponse> getAttemptDetail(
            @PathVariable UUID attemptId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<AttemptDetailResponse>builder()
                .result(attemptService.getAttemptDetail(attemptId, userDetails.getUser().getId()))
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/admin/exercises/{exerciseId}/attempts")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiRespone<List<AttemptResponse>> getAllAttempts(@PathVariable UUID exerciseId) {
        return ApiRespone.<List<AttemptResponse>>builder()
                .result(attemptService.getAllAttempts(exerciseId))
                .code(1000)
                .message("success")
                .build();
    }
}
