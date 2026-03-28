package trung.supper.englishgrammar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.OptionRequest;
import trung.supper.englishgrammar.dto.request.QuestionRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.OptionResponse;
import trung.supper.englishgrammar.dto.response.QuestionResponse;
import trung.supper.englishgrammar.services.IQuestionOptionService;
import trung.supper.englishgrammar.services.IQuestionService;

import java.util.UUID;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class QuestionTeacherController {

    private final IQuestionService questionService;
    private final IQuestionOptionService optionService;

    // ─── Question CRUD ───

    @PostMapping("/exercises/{exerciseId}/questions")
    public ApiRespone<QuestionResponse> createQuestion(
            @PathVariable UUID exerciseId,
            @Valid @RequestBody QuestionRequest request) {
        return ApiRespone.<QuestionResponse>builder()
                .result(questionService.createQuestion(exerciseId, request))
                .code(1000)
                .message("Successfully created question")
                .build();
    }

    @PutMapping("/questions/{id}")
    public ApiRespone<QuestionResponse> updateQuestion(
            @PathVariable UUID id,
            @Valid @RequestBody QuestionRequest request) {
        return ApiRespone.<QuestionResponse>builder()
                .result(questionService.updateQuestion(id, request))
                .code(1000)
                .message("Successfully updated question")
                .build();
    }

    @DeleteMapping("/questions/{id}")
    public ApiRespone<Void> deleteQuestion(@PathVariable UUID id) {
        questionService.deleteQuestion(id);
        return ApiRespone.<Void>builder()
                .code(1000)
                .message("Successfully deleted question")
                .build();
    }

    // ─── Option CRUD ───

    @PostMapping("/questions/{id}/options")
    public ApiRespone<OptionResponse> addOption(
            @PathVariable UUID id,
            @Valid @RequestBody OptionRequest request) {
        return ApiRespone.<OptionResponse>builder()
                .result(optionService.addOption(id, request))
                .code(1000)
                .message("Successfully added option")
                .build();
    }

    @PutMapping("/questions/{id}/options/{optionId}")
    public ApiRespone<OptionResponse> updateOption(
            @PathVariable UUID id,
            @PathVariable UUID optionId,
            @Valid @RequestBody OptionRequest request) {
        return ApiRespone.<OptionResponse>builder()
                .result(optionService.updateOption(id, optionId, request))
                .code(1000)
                .message("Successfully updated option")
                .build();
    }

    @DeleteMapping("/questions/{id}/options/{optionId}")
    public ApiRespone<Void> deleteOption(
            @PathVariable UUID id,
            @PathVariable UUID optionId) {
        optionService.deleteOption(id, optionId);
        return ApiRespone.<Void>builder()
                .code(1000)
                .message("Successfully deleted option")
                .build();
    }
}
