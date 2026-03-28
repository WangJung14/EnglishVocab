package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.ReadingRequest;
import trung.supper.englishgrammar.dto.response.ReadingResponse;

import java.util.UUID;

public interface IReadingPassageService {
    ReadingResponse getReadingPassageByLessonId(UUID lessonId);
    ReadingResponse createReadingPassage(UUID lessonId, ReadingRequest request);
    ReadingResponse updateReadingPassage(UUID lessonId, ReadingRequest request);
    void deleteReadingPassage(UUID lessonId);
}
