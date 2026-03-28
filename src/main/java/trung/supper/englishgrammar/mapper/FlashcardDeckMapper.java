package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import trung.supper.englishgrammar.dto.request.DeckRequest;
import trung.supper.englishgrammar.dto.response.DeckResponse;
import trung.supper.englishgrammar.models.FlashcardDeck;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FlashcardDeckMapper {

    @Mapping(target = "name", source = "title")
    @Mapping(target = "ownerId", source = "createdBy.id")
    DeckResponse toResponse(FlashcardDeck deck);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "flashcards", ignore = true)
    FlashcardDeck toEntity(DeckRequest request);

    @Mapping(target = "title", source = "name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "lesson", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "flashcards", ignore = true)
    void updateEntity(@MappingTarget FlashcardDeck deck, DeckRequest request);
}
