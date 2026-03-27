package trung.supper.englishgrammar.services.strategy.impl;

import org.springframework.stereotype.Component;
import trung.supper.englishgrammar.dto.request.LessonRequest;
import trung.supper.englishgrammar.enums.LessonType;
import trung.supper.englishgrammar.models.Lesson;
import trung.supper.englishgrammar.services.strategy.ILessonStrategy;

@Component
public class ReadingLessonStrategy implements ILessonStrategy {

    @Override
    public LessonType getType() {
        return LessonType.READING;
    }

    @Override
    public void processContent(Lesson lesson, LessonRequest request) {
        // Extend: Manage relations to ReadingPassage optionally embedded in request.
        lesson.setContent(request.getContent());
    }
}
