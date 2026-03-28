package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.QuestionResponse;
import trung.supper.englishgrammar.services.IQuestionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class QuestionPublicController {

    private final IQuestionService questionService;

    @GetMapping("/exercises/{exerciseId}/questions")
    public ApiRespone<List<QuestionResponse>> getQuestionsByExercise(@PathVariable UUID exerciseId) {
        return ApiRespone.<List<QuestionResponse>>builder()
                .result(questionService.getQuestionsByExerciseId(exerciseId))
                .code(1000)
                .message("success")
                .build();
    }
}
