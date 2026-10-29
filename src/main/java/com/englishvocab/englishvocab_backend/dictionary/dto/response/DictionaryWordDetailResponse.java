package com.englishvocab.englishvocab_backend.dictionary.dto.response;

import java.util.List;

public record DictionaryWordDetailResponse(
        Long id,
        String word,
        String languageCode,
        List<DictionarySenseResponse> senses,
        List<DictionaryPronunciationResponse> pronunciations
) {
}