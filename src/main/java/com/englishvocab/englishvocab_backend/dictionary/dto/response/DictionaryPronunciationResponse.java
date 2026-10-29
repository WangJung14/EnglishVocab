package com.englishvocab.englishvocab_backend.dictionary.dto.response;

public record DictionaryPronunciationResponse(
        Long id,
        String ipa,
        String region
) {
}
