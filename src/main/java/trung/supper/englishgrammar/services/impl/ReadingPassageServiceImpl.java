package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.ReadingRequest;
import trung.supper.englishgrammar.dto.response.ReadingResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.mapper.ReadingPassageMapper;
import trung.supper.englishgrammar.models.Lesson;
import trung.supper.englishgrammar.models.ReadingPassage;
import trung.supper.englishgrammar.repositorys.ILessonRepository;
import trung.supper.englishgrammar.repositorys.IReadingPassageRepository;
import trung.supper.englishgrammar.services.IReadingPassageService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadingPassageServiceImpl implements IReadingPassageService {

    private final IReadingPassageRepository readingPassageRepository;
    private final ILessonRepository lessonRepository;
    private final ReadingPassageMapper readingPassageMapper;

    @Override
    @Transactional(readOnly = true)
    public ReadingResponse getReadingPassageByLessonId(UUID lessonId) {
        // Enforce implicit existence
        validateLessonExists(lessonId);
        
        ReadingPassage passage = readingPassageRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        return readingPassageMapper.toResponse(passage);
    }

    @Override
    @Transactional
    public ReadingResponse createReadingPassage(UUID lessonId, ReadingRequest request) {
        Lesson lesson = validateLessonExists(lessonId);

        if (readingPassageRepository.existsByLessonId(lessonId)) {
            // Already has a reading passage! Fail strict 1-1 constraint.
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION); 
        }

        ReadingPassage newPassage = readingPassageMapper.toEntity(request);
        newPassage.setLesson(lesson);

        ReadingPassage saved = readingPassageRepository.save(newPassage);
        return readingPassageMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ReadingResponse updateReadingPassage(UUID lessonId, ReadingRequest request) {
        validateLessonExists(lessonId);
        ReadingPassage existingPassage = readingPassageRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        readingPassageMapper.updateEntity(existingPassage, request);

        ReadingPassage updated = readingPassageRepository.save(existingPassage);
        return readingPassageMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteReadingPassage(UUID lessonId) {
        validateLessonExists(lessonId);
        ReadingPassage existingPassage = readingPassageRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        readingPassageRepository.delete(existingPassage);
    }

    private Lesson validateLessonExists(UUID lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }
}
