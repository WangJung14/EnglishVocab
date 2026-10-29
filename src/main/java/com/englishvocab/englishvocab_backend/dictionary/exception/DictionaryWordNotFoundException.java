package com.englishvocab.englishvocab_backend.dictionary.exception;

public class DictionaryWordNotFoundException
        extends RuntimeException {

    public DictionaryWordNotFoundException(Long id) {
        super("Dictionary word not found with id: " + id);
    }
}