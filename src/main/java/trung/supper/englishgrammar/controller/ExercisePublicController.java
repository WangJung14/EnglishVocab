package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.ExerciseResponse;
import trung.supper.englishgrammar.services.IExerciseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ExercisePublicController {

    private final IExerciseService exerciseService;

    @GetMapping("/lessons/{lessonId}/exercises")
    public ApiRespone<List<ExerciseResponse>> getLessonExercises(@PathVariable UUID lessonId) {
        return ApiRespone.<List<ExerciseResponse>>builder()
                .result(exerciseService.getExercisesByLessonId(lessonId))
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/exercises/{id}")
    public ApiRespone<ExerciseResponse> getExerciseDetails(@PathVariable UUID id) {
        return ApiRespone.<ExerciseResponse>builder()
                .result(exerciseService.getExerciseById(id))
                .code(1000)
                .message("success")
                .build();
    }
}
