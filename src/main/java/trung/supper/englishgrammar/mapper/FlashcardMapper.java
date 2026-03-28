package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import trung.supper.englishgrammar.dto.request.FlashcardRequest;
import trung.supper.englishgrammar.dto.response.FlashcardResponse;
import trung.supper.englishgrammar.models.Flashcard;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FlashcardMapper {

    @Mapping(target = "frontText", source = "front")
    @Mapping(target = "backText", source = "back")
    @Mapping(target = "displayOrder", source = "orderIndex")
    FlashcardResponse toResponse(Flashcard flashcard);

    @Mapping(target = "front", source = "frontText")
    @Mapping(target = "back", source = "backText")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deck", ignore = true)
    @Mapping(target = "word", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Flashcard toEntity(FlashcardRequest request);

    @Mapping(target = "front", source = "frontText")
    @Mapping(target = "back", source = "backText")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deck", ignore = true)
    @Mapping(target = "word", ignore = true)
    @Mapping(target = "orderIndex", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget Flashcard flashcard, FlashcardRequest request);
}
