package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.QuestionRequest;
import trung.supper.englishgrammar.dto.response.QuestionResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.mapper.QuestionMapper;
import trung.supper.englishgrammar.models.Exercise;
import trung.supper.englishgrammar.models.Question;
import trung.supper.englishgrammar.repositorys.IExerciseRepository;
import trung.supper.englishgrammar.repositorys.IQuestionRepository;
import trung.supper.englishgrammar.services.IQuestionService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements IQuestionService {

    private final IQuestionRepository questionRepository;
    private final IExerciseRepository exerciseRepository;
    private final QuestionMapper questionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> getQuestionsByExerciseId(UUID exerciseId) {
        validateExerciseExists(exerciseId);
        List<Question> questions = questionRepository.findByExerciseIdOrderByOrderIndexAsc(exerciseId);
        return questions.stream().map(questionMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public QuestionResponse createQuestion(UUID exerciseId, QuestionRequest request) {
        Exercise exercise = validateExerciseExists(exerciseId);

        Integer currentMaxOrder = questionRepository.findMaxOrderIndexByExerciseId(exerciseId);
        int nextOrderIndex = (currentMaxOrder != null ? currentMaxOrder : 0) + 1;

        Question newQuestion = questionMapper.toEntity(request);
        newQuestion.setExercise(exercise);
        newQuestion.setOrderIndex(nextOrderIndex);

        Question saved = questionRepository.save(newQuestion);
        return questionMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public QuestionResponse updateQuestion(UUID questionId, QuestionRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        questionMapper.updateEntity(question, request);
        Question updated = questionRepository.save(question);
        return questionMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteQuestion(UUID questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        questionRepository.delete(question);
    }

    private Exercise validateExerciseExists(UUID exerciseId) {
        return exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }
}
