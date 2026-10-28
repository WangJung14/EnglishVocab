package com.englishvocab.englishvocab_backend.dictionary.repository;

import com.englishvocab.englishvocab_backend.dictionary.entity.DictionaryPronunciation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DictionaryPronunciationRepository extends JpaRepository<DictionaryPronunciation,Long> {
    List<DictionaryPronunciation> findByWordIdOrderByIdAsc(Long wordId);
}