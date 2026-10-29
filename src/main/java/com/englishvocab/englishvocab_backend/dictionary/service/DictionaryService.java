package com.englishvocab.englishvocab_backend.dictionary.service;

import com.englishvocab.englishvocab_backend.dictionary.dto.response.*;
import com.englishvocab.englishvocab_backend.dictionary.entity.DictionaryPronunciation;
import com.englishvocab.englishvocab_backend.dictionary.entity.DictionarySense;
import com.englishvocab.englishvocab_backend.dictionary.entity.DictionaryWord;
import com.englishvocab.englishvocab_backend.dictionary.exception.DictionaryWordNotFoundException;
import com.englishvocab.englishvocab_backend.dictionary.repository.DictionaryPronunciationRepository;
import com.englishvocab.englishvocab_backend.dictionary.repository.DictionarySenseRepository;
import com.englishvocab.englishvocab_backend.dictionary.repository.DictionaryWordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly=true)
public class DictionaryService {
    private  final DictionaryWordRepository wordRepository;
    private final DictionarySenseRepository senseRepository;
    private final DictionaryPronunciationRepository pronunciationRepository;

    public DictionaryService(
            DictionaryWordRepository wordRepository,
            DictionarySenseRepository senseRepository,
            DictionaryPronunciationRepository pronunciationRepository
    ){
        this.wordRepository = wordRepository;
        this.senseRepository = senseRepository;
        this.pronunciationRepository = pronunciationRepository;
    }

    /*
    * Search Word
    */

    public PageResponse<DictionaryWordSummaryResponse> searchWords(
            String search,
            int page,
            int size
    ){
        if(page < 0){
            throw new IllegalArgumentException("Page must be greater than or equal to 0");
        }

        if(size < 1 || size > 100){
            throw new IllegalArgumentException("Size must be between 1 and 100");
        }

        if(search != null && search.length() > 100){
            throw new IllegalArgumentException("Search term must not exceed 100 characters");
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(
                        Sort.Order.desc("word"),
                        Sort.Order.asc("id")
                )
        );

        Page<DictionaryWord> result;

        if(search == null || search.isBlank()){
            result = wordRepository.findAll(pageable);
        }else{
            result = wordRepository.findByWordStartingWithIgnoreCase(
                    search.trim(),
                    pageable
            );
        }
        Page<DictionaryWordSummaryResponse> mapped =
                result.map(this::toSummaryResponse);

        return PageResponse.from(mapped);
    }
    // 2. GET WORD DETAIL

    public DictionaryWordDetailResponse getWordById(Long id) {

        DictionaryWord word = wordRepository.findById(id)
                .orElseThrow(() ->
                        new DictionaryWordNotFoundException(id)
                );

        List<DictionarySenseResponse> senses =
                senseRepository
                        .findByWordIdOrderByIdAsc(id)
                        .stream()
                        .map(this::toSenseResponse)
                        .toList();

        List<DictionaryPronunciationResponse> pronunciations =
                pronunciationRepository
                        .findByWordIdOrderByIdAsc(id)
                        .stream()
                        .map(this::toPronunciationResponse)
                        .toList();

        return new DictionaryWordDetailResponse(
                word.getId(),
                word.getWord(),
                word.getLanguageCode(),
                senses,
                pronunciations
        );
    }

    // 3. EXACT WORD LOOKUP

    public List<DictionaryWordDetailResponse> findExactWord(
            String word
    ) {
        if (word == null || word.isBlank()) {
            throw new IllegalArgumentException(
                    "Word must not be blank"
            );
        }

        if (word.length() > 100) {
            throw new IllegalArgumentException(
                    "Word must not exceed 100 characters"
            );
        }

        List<DictionaryWord> words =
                wordRepository.findAllByWordOrderByIdAsc(word);

        return words.stream()
                .map(found -> getWordById(found.getId()))
                .toList();
    }

    // 4. ENTITY -> DTO MAPPING

    private DictionaryWordSummaryResponse toSummaryResponse(
            DictionaryWord word
    ) {
        return new DictionaryWordSummaryResponse(
                word.getId(),
                word.getWord(),
                word.getLanguageCode()
        );
    }

    private DictionarySenseResponse toSenseResponse(
            DictionarySense sense
    ) {
        return new DictionarySenseResponse(
                sense.getId(),
                sense.getMeaningVi(),
                sense.getPartOfSpeechRaw(),
                sense.getExample()
        );
    }

    private DictionaryPronunciationResponse toPronunciationResponse(
            DictionaryPronunciation pronunciation
    ) {
        return new DictionaryPronunciationResponse(
                pronunciation.getId(),
                pronunciation.getIpa(),
                pronunciation.getRegionRaw()
        );
    }
}
