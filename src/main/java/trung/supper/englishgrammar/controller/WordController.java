package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.WordResponse;
import trung.supper.englishgrammar.enums.CefrLevel;
import trung.supper.englishgrammar.enums.PartOfSpeech;
import trung.supper.englishgrammar.services.IWordService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/words")
@RequiredArgsConstructor
public class WordController {

    private final IWordService wordService;

    @GetMapping
    public ApiRespone<List<WordResponse>> getWords(
            @RequestParam(required = false) CefrLevel level,
            @RequestParam(required = false) PartOfSpeech partOfSpeech) {
        return ApiRespone.<List<WordResponse>>builder()
                .result(wordService.getWords(level, partOfSpeech))
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/{id}")
    public ApiRespone<WordResponse> getWordById(@PathVariable UUID id) {
        return ApiRespone.<WordResponse>builder()
                .result(wordService.getWordById(id))
                .code(1000)
                .message("success")
                .build();
    }
}
