package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import trung.supper.englishgrammar.dto.response.LessonWordResponse;
import trung.supper.englishgrammar.models.LessonWord;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonWordMapper {

    @Mapping(target = "id", source = "word.id")
    @Mapping(target = "word", source = "word.word")
    @Mapping(target = "meaning", source = "word.definitionVi")
    @Mapping(target = "displayOrder", source = "orderIndex")
    LessonWordResponse toResponse(LessonWord lessonWord);

    List<LessonWordResponse> toResponseList(List<LessonWord> lessonWords);
}
