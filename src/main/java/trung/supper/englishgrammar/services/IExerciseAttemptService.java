package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.SubmitAttemptRequest;
import trung.supper.englishgrammar.dto.response.AttemptDetailResponse;
import trung.supper.englishgrammar.dto.response.AttemptResponse;
import trung.supper.englishgrammar.dto.response.StartAttemptResponse;

import java.util.List;
import java.util.UUID;

public interface IExerciseAttemptService {

    StartAttemptResponse startAttempt(UUID exerciseId, UUID userId);

    AttemptResponse submitAttempt(UUID exerciseId, UUID userId, SubmitAttemptRequest request);

    List<AttemptResponse> getMyAttempts(UUID exerciseId, UUID userId);

    AttemptDetailResponse getAttemptDetail(UUID attemptId, UUID userId);

    // Admin
    List<AttemptResponse> getAllAttempts(UUID exerciseId);
}
