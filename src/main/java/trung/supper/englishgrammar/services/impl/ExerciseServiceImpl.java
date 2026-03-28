package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.ExerciseRequest;
import trung.supper.englishgrammar.dto.request.ReorderExercisesRequest;
import trung.supper.englishgrammar.dto.response.ExerciseResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.mapper.ExerciseMapper;
import trung.supper.englishgrammar.models.Exercise;
import trung.supper.englishgrammar.models.Lesson;
import trung.supper.englishgrammar.repositorys.IExerciseRepository;
import trung.supper.englishgrammar.repositorys.ILessonRepository;
import trung.supper.englishgrammar.services.IExerciseService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements IExerciseService {

    private final IExerciseRepository exerciseRepository;
    private final ILessonRepository lessonRepository;
    private final ExerciseMapper exerciseMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ExerciseResponse> getExercisesByLessonId(UUID lessonId) {
        validateLessonExists(lessonId);
        List<Exercise> exercises = exerciseRepository.findByLessonIdOrderByOrderIndexAsc(lessonId);
        return exercises.stream().map(exerciseMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ExerciseResponse getExerciseById(UUID exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return exerciseMapper.toResponse(exercise);
    }

    @Override
    @Transactional
    public ExerciseResponse createExercise(UUID lessonId, ExerciseRequest request) {
        Lesson lesson = validateLessonExists(lessonId);

        Integer currentMaxOrder = exerciseRepository.findMaxOrderIndexByLessonId(lessonId);
        int nextOrderIndex = (currentMaxOrder != null ? currentMaxOrder : 0) + 1;

        Exercise newExercise = exerciseMapper.toEntity(request);
        newExercise.setLesson(lesson);
        newExercise.setOrderIndex(nextOrderIndex);

        Exercise saved = exerciseRepository.save(newExercise);
        return exerciseMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ExerciseResponse updateExercise(UUID exerciseId, ExerciseRequest request) {
        Exercise existingExercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        exerciseMapper.updateEntity(existingExercise, request);
        Exercise updated = exerciseRepository.save(existingExercise);
        
        return exerciseMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteExercise(UUID exerciseId) {
        Exercise existingExercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        exerciseRepository.delete(existingExercise);
    }

    @Override
    @Transactional
    public List<ExerciseResponse> reorderExercises(UUID lessonId, ReorderExercisesRequest request) {
        validateLessonExists(lessonId);
        List<UUID> newOrderWordIds = request.getExerciseIds();

        List<Exercise> existingExercises = exerciseRepository.findByLessonIdOrderByOrderIndexAsc(lessonId);
        
        if (existingExercises.size() != newOrderWordIds.size()) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION); 
        }

        Map<UUID, Exercise> exerciseMapById = existingExercises.stream()
                .collect(Collectors.toMap(Exercise::getId, e -> e));

        List<Exercise> updatedExercises = new ArrayList<>();
        int index = 1;

        for (UUID id : newOrderWordIds) {
            Exercise e = exerciseMapById.get(id);
            if (e == null) {
                throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
            }
            e.setOrderIndex(index++);
            updatedExercises.add(exerciseRepository.save(e));
        }

        return updatedExercises.stream().map(exerciseMapper::toResponse).collect(Collectors.toList());
    }

    private Lesson validateLessonExists(UUID lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }
}
