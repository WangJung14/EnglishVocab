package com.englishvocab.englishvocab_backend.dictionary.dto.response;

public record DictionarySenseResponse(
        Long id,
        String meaningVi,
        String partOfSpeech,
        String example
) {
}
