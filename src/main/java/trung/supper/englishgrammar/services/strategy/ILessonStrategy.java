package trung.supper.englishgrammar.services.strategy;

import trung.supper.englishgrammar.dto.request.LessonRequest;
import trung.supper.englishgrammar.enums.LessonType;
import trung.supper.englishgrammar.models.Lesson;

/**
 * Interface implementing the Strategy Design Pattern allowing
 * parsing, validating, and structuring lesson content dynamically
 * without altering the main class execution flow.
 */
public interface ILessonStrategy {
    LessonType getType();
    void processContent(Lesson lesson, LessonRequest request);
}
