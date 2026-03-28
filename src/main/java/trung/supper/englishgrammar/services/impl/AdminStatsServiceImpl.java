package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.response.AdminStatsResponse;
import trung.supper.englishgrammar.dto.response.ContentStatsResponse;
import trung.supper.englishgrammar.dto.response.UserGrowthResponse;
import trung.supper.englishgrammar.repositorys.IExerciseRepository;
import trung.supper.englishgrammar.repositorys.ILessonRepository;
import trung.supper.englishgrammar.repositorys.IUserExerciseAttemptRepository;
import trung.supper.englishgrammar.repositorys.IUserRepository;
import trung.supper.englishgrammar.services.IAdminStatsService;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminStatsServiceImpl implements IAdminStatsService {

    private final IUserRepository userRepository;
    private final ILessonRepository lessonRepository;
    private final IExerciseRepository exerciseRepository;
    private final IUserExerciseAttemptRepository attemptRepository;

    // ─── Overview ───

    @Override
    @Transactional(readOnly = true)
    public AdminStatsResponse getOverviewStats() {
        return AdminStatsResponse.builder()
                .totalUsers(userRepository.count())
                .totalLessons(lessonRepository.count())
                .totalExercises(exerciseRepository.count())
                .totalAttempts(attemptRepository.count())
                .build();
    }

    // ─── User Growth ───

    @Override
    @Transactional(readOnly = true)
    public List<UserGrowthResponse> getUserGrowth(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<Object[]> rows = userRepository.findUserGrowthSince(since);

        List<UserGrowthResponse> result = new ArrayList<>();
        for (Object[] row : rows) {
            // row[0] = DATE string, row[1] = count
            LocalDate date = LocalDate.parse(row[0].toString());
            long newUsers = ((BigInteger) row[1]).longValue();
            result.add(UserGrowthResponse.builder()
                    .date(date)
                    .newUsers(newUsers)
                    .build());
        }
        return result;
    }

    // ─── Content Stats ───

    @Override
    @Transactional(readOnly = true)
    public List<ContentStatsResponse> getContentStats(int limit) {
        List<ContentStatsResponse> result = new ArrayList<>();

        // Top popular lessons (by start count)
        List<Object[]> lessonRows = lessonRepository.findMostPopularLessons(limit);
        for (Object[] row : lessonRows) {
            result.add(ContentStatsResponse.builder()
                    .id(UUID.fromString(row[0].toString()))
                    .type("LESSON")
                    .count(((BigInteger) row[1]).longValue())
                    .build());
        }

        // Top attempted exercises
        List<Object[]> exerciseRows = exerciseRepository.findMostAttemptedExercises(limit);
        for (Object[] row : exerciseRows) {
            result.add(ContentStatsResponse.builder()
                    .id(UUID.fromString(row[0].toString()))
                    .type("EXERCISE")
                    .count(((BigInteger) row[1]).longValue())
                    .build());
        }

        return result;
    }
}
