package com.englishvocab.englishvocab_backend.dictionary.dto.response;

public record DictionaryWordSummaryResponse(
        Long id,
        String word,
        String languageCode
) {
}
