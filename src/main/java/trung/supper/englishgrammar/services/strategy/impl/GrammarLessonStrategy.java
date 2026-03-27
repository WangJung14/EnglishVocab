package trung.supper.englishgrammar.services.strategy.impl;

import org.springframework.stereotype.Component;
import trung.supper.englishgrammar.dto.request.LessonRequest;
import trung.supper.englishgrammar.enums.LessonType;
import trung.supper.englishgrammar.models.Lesson;
import trung.supper.englishgrammar.services.strategy.ILessonStrategy;

@Component
public class GrammarLessonStrategy implements ILessonStrategy {

    @Override
    public LessonType getType() {
        return LessonType.GRAMMAR;
    }

    @Override
    public void processContent(Lesson lesson, LessonRequest request) {
        // Extend: Logic for strictly validating or altering grammar JSON metadata.
        lesson.setContent(request.getContent());
    }
}
