package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.UpdateProgressRequest;
import trung.supper.englishgrammar.dto.response.LessonProgressResponse;
import trung.supper.englishgrammar.dto.response.TopicProgressResponse;

import java.util.List;
import java.util.UUID;

public interface ILessonProgressService {
    List<LessonProgressResponse> getAllProgress(UUID userId);
    TopicProgressResponse getTopicProgress(UUID userId, UUID topicId);
    LessonProgressResponse startLesson(UUID userId, UUID lessonId);
    LessonProgressResponse updateProgress(UUID userId, UUID lessonId, UpdateProgressRequest request);
    LessonProgressResponse completeLesson(UUID userId, UUID lessonId);
}
