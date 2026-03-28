package trung.supper.englishgrammar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.ExerciseRequest;
import trung.supper.englishgrammar.dto.request.ReorderExercisesRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.ExerciseResponse;
import trung.supper.englishgrammar.services.IExerciseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
public class ExerciseTeacherController {

    private final IExerciseService exerciseService;

    @PostMapping("/lessons/{lessonId}/exercises")
    public ApiRespone<ExerciseResponse> createExercise(
            @PathVariable UUID lessonId,
            @Valid @RequestBody ExerciseRequest request) {
        return ApiRespone.<ExerciseResponse>builder()
                .result(exerciseService.createExercise(lessonId, request))
                .code(1000)
                .message("Successfully created exercise")
                .build();
    }

    @PutMapping("/exercises/{id}")
    public ApiRespone<ExerciseResponse> updateExercise(
            @PathVariable UUID id,
            @Valid @RequestBody ExerciseRequest request) {
        return ApiRespone.<ExerciseResponse>builder()
                .result(exerciseService.updateExercise(id, request))
                .code(1000)
                .message("Successfully updated exercise")
                .build();
    }

    @DeleteMapping("/exercises/{id}")
    public ApiRespone<Void> deleteExercise(@PathVariable UUID id) {
        exerciseService.deleteExercise(id);
        return ApiRespone.<Void>builder()
                .code(1000)
                .message("Successfully deleted exercise")
                .build();
    }

    @PutMapping("/lessons/{lessonId}/exercises/reorder")
    public ApiRespone<List<ExerciseResponse>> reorderExercises(
            @PathVariable UUID lessonId,
            @Valid @RequestBody ReorderExercisesRequest request) {
        return ApiRespone.<List<ExerciseResponse>>builder()
                .result(exerciseService.reorderExercises(lessonId, request))
                .code(1000)
                .message("Successfully reordered exercises")
                .build();
    }
}
