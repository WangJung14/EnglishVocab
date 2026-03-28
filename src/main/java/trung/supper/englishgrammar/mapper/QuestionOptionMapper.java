package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import trung.supper.englishgrammar.dto.request.OptionRequest;
import trung.supper.englishgrammar.dto.response.OptionResponse;
import trung.supper.englishgrammar.models.QuestionOption;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface QuestionOptionMapper {

    OptionResponse toResponse(QuestionOption option);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "matchPairId", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    QuestionOption toEntity(OptionRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "question", ignore = true)
    @Mapping(target = "matchPairId", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    void updateEntity(@MappingTarget QuestionOption option, OptionRequest request);
}
