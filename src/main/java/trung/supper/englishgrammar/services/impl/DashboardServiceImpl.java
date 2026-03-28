package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.response.DashboardResponse;
import trung.supper.englishgrammar.enums.ProgressStatus;
import trung.supper.englishgrammar.models.User;
import trung.supper.englishgrammar.models.UserStreak;
import trung.supper.englishgrammar.repositorys.IUserLessonProgressRepository;
import trung.supper.englishgrammar.repositorys.IUserStreakRepository;
import trung.supper.englishgrammar.services.IDashboardService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements IDashboardService {

    private final IUserLessonProgressRepository progressRepository;
    private final IUserStreakRepository streakRepository;

    @Override
    @Transactional
    public DashboardResponse getDashboard(UUID userId) {
        // ─── Completed counts ───
        long totalLessonsCompleted = progressRepository.countByUserIdAndStatus(userId, ProgressStatus.COMPLETED);
        long totalTopicsCompleted = progressRepository.countDistinctCompletedTopicsByUserId(userId, ProgressStatus.COMPLETED);

        // ─── Streak: recompute from completion dates, then persist ───
        UserStreak streak = computeAndPersistStreak(userId);

        return DashboardResponse.builder()
                .totalLessonsCompleted(totalLessonsCompleted)
                .totalTopicsCompleted(totalTopicsCompleted)
                .currentStreak(streak.getCurrentStreak())
                .longestStreak(streak.getLongestStreak())
                .build();
    }

    /**
     * Computes current and longest streak from the set of distinct days the user
     * completed at least one lesson. Updates and persists the UserStreak record.
     *
     * Streak rules:
     *  - consecutive calendar days (today, yesterday, day before…) count as streak
     *  - if user has activity today → streak is alive
     *  - if last activity was yesterday → streak is alive
     *  - if last activity was before yesterday → streak resets to 0
     */
    private UserStreak computeAndPersistStreak(UUID userId) {
        List<LocalDateTime> completedAtList = progressRepository
                .findCompletedAtByUserId(userId, ProgressStatus.COMPLETED);

        Set<LocalDate> activeDays = completedAtList.stream()
                .map(LocalDateTime::toLocalDate)
                .collect(Collectors.toSet());

        int currentStreak = calculateCurrentStreak(activeDays);
        int longestStreak = calculateLongestStreak(activeDays);

        // Persist streak record (upsert)
        UserStreak streak = streakRepository.findByUserId(userId).orElseGet(() -> {
            User userRef = new User();
            userRef.setId(userId);
            return UserStreak.builder()
                    .userId(userId)
                    .user(userRef)
                    .currentStreak(0)
                    .longestStreak(0)
                    .build();
        });

        streak.setCurrentStreak(currentStreak);
        streak.setLongestStreak(longestStreak);
        streak.setLastActivityDate(activeDays.isEmpty() ? null
                : activeDays.stream().max(LocalDate::compareTo).orElse(null));

        return streakRepository.save(streak);
    }

    /**
     * Counts backwards from today through consecutive days that have activity.
     */
    private int calculateCurrentStreak(Set<LocalDate> activeDays) {
        if (activeDays.isEmpty()) return 0;

        LocalDate cursor = LocalDate.now();

        // If user hasn't had activity today or yesterday, streak is 0
        if (!activeDays.contains(cursor) && !activeDays.contains(cursor.minusDays(1))) {
            return 0;
        }

        // Walk backwards counting consecutive days
        int streak = 0;
        while (activeDays.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    /**
     * Finds the longest consecutive-day streak anywhere in the user's history.
     */
    private int calculateLongestStreak(Set<LocalDate> activeDays) {
        if (activeDays.isEmpty()) return 0;

        List<LocalDate> sortedDays = activeDays.stream()
                .sorted()
                .collect(java.util.stream.Collectors.toList());

        int longest = 1;
        int current = 1;

        for (int i = 1; i < sortedDays.size(); i++) {
            if (sortedDays.get(i).equals(sortedDays.get(i - 1).plusDays(1))) {
                current++;
                longest = Math.max(longest, current);
            } else {
                current = 1;
            }
        }
        return longest;
    }
}
