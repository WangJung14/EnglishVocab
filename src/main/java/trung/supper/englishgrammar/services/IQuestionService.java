package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.QuestionRequest;
import trung.supper.englishgrammar.dto.response.QuestionResponse;

import java.util.List;
import java.util.UUID;

public interface IQuestionService {
    List<QuestionResponse> getQuestionsByExerciseId(UUID exerciseId);
    QuestionResponse createQuestion(UUID exerciseId, QuestionRequest request);
    QuestionResponse updateQuestion(UUID questionId, QuestionRequest request);
    void deleteQuestion(UUID questionId);
}
