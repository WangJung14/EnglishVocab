package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.ExerciseRequest;
import trung.supper.englishgrammar.dto.request.ReorderExercisesRequest;
import trung.supper.englishgrammar.dto.response.ExerciseResponse;

import java.util.List;
import java.util.UUID;

public interface IExerciseService {
    List<ExerciseResponse> getExercisesByLessonId(UUID lessonId);
    ExerciseResponse getExerciseById(UUID exerciseId);
    ExerciseResponse createExercise(UUID lessonId, ExerciseRequest request);
    ExerciseResponse updateExercise(UUID exerciseId, ExerciseRequest request);
    void deleteExercise(UUID exerciseId);
    List<ExerciseResponse> reorderExercises(UUID lessonId, ReorderExercisesRequest request);
}
