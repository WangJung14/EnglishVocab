package com.englishvocab.englishvocab_backend.dictionary.controller;

import com.englishvocab.englishvocab_backend.dictionary.dto.response.DictionaryWordDetailResponse;
import com.englishvocab.englishvocab_backend.dictionary.dto.response.DictionaryWordSummaryResponse;
import com.englishvocab.englishvocab_backend.dictionary.dto.response.PageResponse;
import com.englishvocab.englishvocab_backend.dictionary.service.DictionaryService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dictionary/words")
public class DictionaryController {

    private final DictionaryService dictionaryService;

    public DictionaryController(
            DictionaryService dictionaryService
    ) {
        this.dictionaryService = dictionaryService;
    }

    // GET /api/v1/dictionary/words
    @GetMapping
    public PageResponse<DictionaryWordSummaryResponse> searchWords(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return dictionaryService.searchWords(
                search,
                page,
                size
        );
    }

    // GET /api/v1/dictionary/words/exact
    @GetMapping("/exact")
    public List<DictionaryWordDetailResponse> findExactWord(
            @RequestParam String word
    ) {
        return dictionaryService.findExactWord(word);
    }

    // GET /api/v1/dictionary/words/{id}
    @GetMapping("/{id}")
    public DictionaryWordDetailResponse getWordById(
            @PathVariable Long id
    ) {
        return dictionaryService.getWordById(id);
    }
}