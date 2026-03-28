package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import trung.supper.englishgrammar.dto.request.ReadingRequest;
import trung.supper.englishgrammar.dto.response.ReadingResponse;
import trung.supper.englishgrammar.models.ReadingPassage;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReadingPassageMapper {

    ReadingResponse toResponse(ReadingPassage readingPassage);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "audioUrl", ignore = true)
    @Mapping(target = "source", ignore = true)
    @Mapping(target = "wordCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ReadingPassage toEntity(ReadingRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "audioUrl", ignore = true)
    @Mapping(target = "source", ignore = true)
    @Mapping(target = "wordCount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget ReadingPassage readingPassage, ReadingRequest request);
}
