package com.englishvocab.englishvocab_backend.dictionary.repository;

import com.englishvocab.englishvocab_backend.dictionary.entity.DictionarySense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DictionarySenseRepository extends JpaRepository<DictionarySense, Long> {
    List<DictionarySense> findByWordIdOrderByIdAsc(Long wordId);
}