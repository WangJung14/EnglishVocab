package trung.supper.englishgrammar.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import trung.supper.englishgrammar.dto.request.WordRequest;
import trung.supper.englishgrammar.dto.response.WordResponse;
import trung.supper.englishgrammar.models.Word;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface WordMapper {

    @Mapping(source = "definitionVi", target = "meaning")
    @Mapping(source = "phonetic", target = "pronunciation")
    @Mapping(source = "exampleSentence", target = "example")
    WordResponse toWordResponse(Word word);

    @Mapping(source = "meaning", target = "definitionVi")
    @Mapping(source = "pronunciation", target = "phonetic")
    @Mapping(source = "example", target = "exampleSentence")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "audioUrl", ignore = true)
    @Mapping(target = "exampleTransaction", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Word toWord(WordRequest request);

    @Mapping(source = "meaning", target = "definitionVi")
    @Mapping(source = "pronunciation", target = "phonetic")
    @Mapping(source = "example", target = "exampleSentence")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "audioUrl", ignore = true)
    @Mapping(target = "exampleTransaction", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateWord(@MappingTarget Word word, WordRequest request);
}
