package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import trung.supper.englishgrammar.dto.request.ExerciseRequest;
import trung.supper.englishgrammar.dto.response.ExerciseResponse;
import trung.supper.englishgrammar.models.Exercise;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ExerciseMapper {

    @Mapping(target = "displayOrder", source = "orderIndex")
    ExerciseResponse toResponse(Exercise exercise);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "passScore", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "timeLimitSeconds", source = "timeLimit")
    Exercise toEntity(ExerciseRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "passScore", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "timeLimitSeconds", source = "timeLimit")
    void updateEntity(@MappingTarget Exercise exercise, ExerciseRequest request);
}
