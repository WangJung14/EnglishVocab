package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.WordRequest;
import trung.supper.englishgrammar.dto.response.WordResponse;
import trung.supper.englishgrammar.enums.CefrLevel;
import trung.supper.englishgrammar.enums.PartOfSpeech;

import java.util.List;
import java.util.UUID;

public interface IWordService {
    List<WordResponse> getWords(CefrLevel level, PartOfSpeech partOfSpeech);
    WordResponse getWordById(UUID id);
    WordResponse createWord(WordRequest request);
    WordResponse updateWord(UUID id, WordRequest request);
    void deleteWord(UUID id);
}
