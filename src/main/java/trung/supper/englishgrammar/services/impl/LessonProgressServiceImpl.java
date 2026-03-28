package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.UpdateProgressRequest;
import trung.supper.englishgrammar.dto.response.LessonProgressResponse;
import trung.supper.englishgrammar.dto.response.TopicProgressResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.enums.ProgressStatus;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.models.Lesson;
import trung.supper.englishgrammar.models.User;
import trung.supper.englishgrammar.models.UserLessonProgress;
import trung.supper.englishgrammar.repositorys.ILessonRepository;
import trung.supper.englishgrammar.repositorys.IUserLessonProgressRepository;
import trung.supper.englishgrammar.services.ILessonProgressService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonProgressServiceImpl implements ILessonProgressService {

    private final IUserLessonProgressRepository progressRepository;
    private final ILessonRepository lessonRepository;

    // ─── Query ───

    @Override
    @Transactional(readOnly = true)
    public List<LessonProgressResponse> getAllProgress(UUID userId) {
        return progressRepository.findAllByUserId(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TopicProgressResponse getTopicProgress(UUID userId, UUID topicId) {
        long totalLessons = progressRepository.countTotalLessonsByTopicId(topicId);
        long completedLessons = progressRepository.countByUserIdAndTopicIdAndStatus(
                userId, topicId, ProgressStatus.COMPLETED);

        int percent = totalLessons > 0 ? (int) ((completedLessons * 100) / totalLessons) : 0;

        return TopicProgressResponse.builder()
                .topicId(topicId)
                .totalLessons(totalLessons)
                .completedLessons(completedLessons)
                .progressPercent(percent)
                .build();
    }

    // ─── Start ───

    @Override
    @Transactional
    public LessonProgressResponse startLesson(UUID userId, UUID lessonId) {
        Lesson lesson = validateLessonExists(lessonId);

        // Idempotent — return existing if already started
        return progressRepository.findByUserIdAndLessonId(userId, lessonId)
                .map(this::toResponse)
                .orElseGet(() -> {
                    User userRef = new User();
                    userRef.setId(userId);

                    UserLessonProgress progress = UserLessonProgress.builder()
                            .user(userRef)
                            .lesson(lesson)
                            .status(ProgressStatus.IN_PROGRESS)
                            .progressPercent(0)
                            .startedAt(LocalDateTime.now())
                            .build();

                    UserLessonProgress saved = progressRepository.save(progress);
                    return toResponse(saved);
                });
    }

    // ─── Update ───

    @Override
    @Transactional
    public LessonProgressResponse updateProgress(UUID userId, UUID lessonId, UpdateProgressRequest request) {
        validateLessonExists(lessonId);

        UserLessonProgress progress = progressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        // Prevent progress from decreasing
        if (request.getProgressPercent() > progress.getProgressPercent()) {
            progress.setProgressPercent(request.getProgressPercent());
        }

        UserLessonProgress saved = progressRepository.save(progress);
        return toResponse(saved);
    }

    // ─── Complete ───

    @Override
    @Transactional
    public LessonProgressResponse completeLesson(UUID userId, UUID lessonId) {
        validateLessonExists(lessonId);

        UserLessonProgress progress = progressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        progress.setProgressPercent(100);
        progress.setStatus(ProgressStatus.COMPLETED);
        progress.setCompletedAt(LocalDateTime.now());

        UserLessonProgress saved = progressRepository.save(progress);
        return toResponse(saved);
    }

    // ─── Helpers ───

    private LessonProgressResponse toResponse(UserLessonProgress progress) {
        return LessonProgressResponse.builder()
                .lessonId(progress.getLesson().getId())
                .lessonTitle(progress.getLesson().getTitle())
                .status(progress.getStatus())
                .progressPercent(progress.getProgressPercent())
                .startedAt(progress.getStartedAt())
                .completedAt(progress.getCompletedAt())
                .build();
    }

    private Lesson validateLessonExists(UUID lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }
}
