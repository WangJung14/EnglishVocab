package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import trung.supper.englishgrammar.dto.request.LessonRequest;
import trung.supper.englishgrammar.dto.response.LessonResponse;
import trung.supper.englishgrammar.models.Lesson;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LessonMapper {

    LessonResponse toLessonResponse(Lesson lesson);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "isPublished", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "summary", ignore = true)
    @Mapping(target = "isFree", ignore = true)
    @Mapping(target = "readingPassage", ignore = true)
    @Mapping(target = "lessonWords", ignore = true)
    @Mapping(target = "exercises", ignore = true)
    Lesson toLesson(LessonRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "isPublished", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "topic", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "summary", ignore = true)
    @Mapping(target = "isFree", ignore = true)
    @Mapping(target = "readingPassage", ignore = true)
    @Mapping(target = "lessonWords", ignore = true)
    @Mapping(target = "exercises", ignore = true)
    void updateLesson(@MappingTarget Lesson lesson, LessonRequest request);
}
