package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.response.UserStatsResponse;
import trung.supper.englishgrammar.enums.AttemptStatus;
import trung.supper.englishgrammar.enums.ProgressStatus;
import trung.supper.englishgrammar.repositorys.IUserExerciseAttemptRepository;
import trung.supper.englishgrammar.repositorys.IUserFlashcardSrsRepository;
import trung.supper.englishgrammar.repositorys.IUserLessonProgressRepository;
import trung.supper.englishgrammar.services.IUserStatsService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserStatsServiceImpl implements IUserStatsService {

    private final IUserLessonProgressRepository progressRepository;
    private final IUserExerciseAttemptRepository attemptRepository;
    private final IUserFlashcardSrsRepository srsRepository;

    @Override
    @Transactional(readOnly = true)
    public UserStatsResponse getStats(UUID userId) {
        // ─── Lesson counts (single-query aggregations) ───
        long totalLessonsStarted = progressRepository.countByUserIdAndStatus(userId, ProgressStatus.IN_PROGRESS)
                + progressRepository.countByUserIdAndStatus(userId, ProgressStatus.COMPLETED);

        long totalLessonsCompleted = progressRepository.countByUserIdAndStatus(userId, ProgressStatus.COMPLETED);

        double completionRate = totalLessonsStarted > 0
                ? Math.round((totalLessonsCompleted * 100.0 / totalLessonsStarted) * 10.0) / 10.0
                : 0.0;

        // ─── Exercise score average (from SUBMITTED attempts only) ───
        Double averageScore = attemptRepository.findAverageScoreByUserIdAndStatus(userId, AttemptStatus.SUBMITTED);

        // ─── SRS stats ───
        long totalWordsLearned = srsRepository.countLearnedByUserId(userId);
        long reviewCount = srsRepository.countByUserId(userId);

        return UserStatsResponse.builder()
                .totalLessonsStarted(totalLessonsStarted)
                .totalLessonsCompleted(totalLessonsCompleted)
                .completionRate(completionRate)
                .averageScore(averageScore)
                .totalWordsLearned(totalWordsLearned)
                .reviewCount(reviewCount)
                .build();
    }
}
