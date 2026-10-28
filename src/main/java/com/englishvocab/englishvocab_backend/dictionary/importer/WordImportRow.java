package com.englishvocab.englishvocab_backend.dictionary.importer;

public record WordImportRow(
        long sourceWordId,
        String word,
        String languageCode
) {
}
