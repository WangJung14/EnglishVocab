package com.englishvocab.englishvocab_backend.dictionary.repository;


import com.englishvocab.englishvocab_backend.dictionary.entity.DictionaryWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DictionaryWordRepository extends JpaRepository<DictionaryWord, Long> {
    Page<DictionaryWord> findByWordStartingWithIgnoreCase(
            String prefix,
            Pageable pageable
    );

    List<DictionaryWord> findAllByWordOrderByIdAsc(
            String word
    );
}