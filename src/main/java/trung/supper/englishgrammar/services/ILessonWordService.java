package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.AddWordToLessonRequest;
import trung.supper.englishgrammar.dto.request.ReorderLessonWordsRequest;
import trung.supper.englishgrammar.dto.response.LessonWordResponse;

import java.util.List;
import java.util.UUID;

public interface ILessonWordService {
    
    List<LessonWordResponse> getWordsForLesson(UUID lessonId);

    LessonWordResponse addWordToLesson(UUID lessonId, AddWordToLessonRequest request);

    void removeWordFromLesson(UUID lessonId, UUID wordId);

    List<LessonWordResponse> reorderWords(UUID lessonId, ReorderLessonWordsRequest request);
}
