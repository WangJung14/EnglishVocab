package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.AddWordToLessonRequest;
import trung.supper.englishgrammar.dto.request.ReorderLessonWordsRequest;
import trung.supper.englishgrammar.dto.response.LessonWordResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.mapper.LessonWordMapper;
import trung.supper.englishgrammar.models.Lesson;
import trung.supper.englishgrammar.models.LessonWord;
import trung.supper.englishgrammar.models.Word;
import trung.supper.englishgrammar.repositorys.ILessonRepository;
import trung.supper.englishgrammar.repositorys.ILessonWordRepository;
import trung.supper.englishgrammar.repositorys.IWordRepository;
import trung.supper.englishgrammar.services.ILessonWordService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonWordServiceImpl implements ILessonWordService {

    private final ILessonWordRepository lessonWordRepository;
    private final ILessonRepository lessonRepository;
    private final IWordRepository wordRepository;
    private final LessonWordMapper lessonWordMapper;

    @Override
    @Transactional(readOnly = true)
    public List<LessonWordResponse> getWordsForLesson(UUID lessonId) {
        validateLessonExists(lessonId);
        List<LessonWord> lessonWords = lessonWordRepository.findByLessonIdOrderByOrderIndexAsc(lessonId);
        return lessonWordMapper.toResponseList(lessonWords);
    }

    @Override
    @Transactional
    public LessonWordResponse addWordToLesson(UUID lessonId, AddWordToLessonRequest request) {
        Lesson lesson = validateLessonExists(lessonId);

        Word word = wordRepository.findById(request.getWordId())
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        if (lessonWordRepository.existsByLessonIdAndWordId(lessonId, word.getId())) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION); // Could use a custom DUPLICATE_ENTRY code, using generic for now to not break constraints
        }

        Integer currentMaxOrder = lessonWordRepository.findMaxOrderIndexByLessonId(lessonId);
        int nextOrderIndex = (currentMaxOrder != null ? currentMaxOrder : 0) + 1;

        LessonWord newLessonWord = LessonWord.builder()
                .lesson(lesson)
                .word(word)
                .orderIndex(nextOrderIndex)
                .build();

        LessonWord saved = lessonWordRepository.save(newLessonWord);
        return lessonWordMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void removeWordFromLesson(UUID lessonId, UUID wordId) {
        validateLessonExists(lessonId);
        LessonWord lessonWord = lessonWordRepository.findByLessonIdAndWordId(lessonId, wordId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        Integer orderIndex = lessonWord.getOrderIndex();
        lessonWordRepository.delete(lessonWord);

        // Shift remaining items safely
        lessonWordRepository.decrementOrderIndexAfter(lessonId, orderIndex);
    }

    @Override
    @Transactional
    public List<LessonWordResponse> reorderWords(UUID lessonId, ReorderLessonWordsRequest request) {
        validateLessonExists(lessonId);
        List<UUID> newOrderWordIds = request.getWordIds();

        List<LessonWord> existingLessonWords = lessonWordRepository.findByLessonIdOrderByOrderIndexAsc(lessonId);
        
        if (existingLessonWords.size() != newOrderWordIds.size()) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION); // Mismatch in payload length
        }

        Map<UUID, LessonWord> lessonWordMapByWordId = existingLessonWords.stream()
                .collect(Collectors.toMap(lw -> lw.getWord().getId(), lw -> lw));

        List<LessonWord> updatedLessonWords = new ArrayList<>();
        int index = 1;
        
        for (UUID wordId : newOrderWordIds) {
            LessonWord lw = lessonWordMapByWordId.get(wordId);
            if (lw == null) {
                throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
            }
            lw.setOrderIndex(index++);
            updatedLessonWords.add(lessonWordRepository.save(lw));
        }

        return lessonWordMapper.toResponseList(updatedLessonWords);
    }

    private Lesson validateLessonExists(UUID lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }
}
