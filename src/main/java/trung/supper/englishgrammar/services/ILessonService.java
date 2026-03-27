package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.LessonReorderRequest;
import trung.supper.englishgrammar.dto.request.LessonRequest;
import trung.supper.englishgrammar.dto.response.LessonResponse;

import java.util.List;
import java.util.UUID;

public interface ILessonService {
    List<LessonResponse> getLessonsByTopicSlug(String topicSlug);
    LessonResponse getLessonBySlug(String slug);
    LessonResponse createLesson(LessonRequest request);
    LessonResponse updateLesson(UUID id, LessonRequest request);
    void deleteLesson(UUID id);
    LessonResponse togglePublish(UUID id);
    void reorderLessons(LessonReorderRequest request);
}
