package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import trung.supper.englishgrammar.dto.request.QuestionRequest;
import trung.supper.englishgrammar.dto.response.QuestionResponse;
import trung.supper.englishgrammar.models.Question;

@Mapper(componentModel = "spring", uses = {QuestionOptionMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface QuestionMapper {

    @Mapping(target = "type", source = "questionType")
    QuestionResponse toResponse(Question question);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exercise", ignore = true)
    @Mapping(target = "explanation", ignore = true)
    @Mapping(target = "scoreWeight", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "options", ignore = true)
    @Mapping(target = "questionType", source = "type")
    Question toEntity(QuestionRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "exercise", ignore = true)
    @Mapping(target = "explanation", ignore = true)
    @Mapping(target = "scoreWeight", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "options", ignore = true)
    @Mapping(target = "questionType", source = "type")
    void updateEntity(@MappingTarget Question question, QuestionRequest request);
}
