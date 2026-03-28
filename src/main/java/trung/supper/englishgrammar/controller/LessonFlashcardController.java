package trung.supper.englishgrammar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.DeckResponse;
import trung.supper.englishgrammar.services.IFlashcardDeckService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LessonFlashcardController {

    private final IFlashcardDeckService deckService;

    @GetMapping("/lessons/{lessonId}/flashcard-deck")
    public ApiRespone<DeckResponse> getDeckByLesson(@PathVariable UUID lessonId) {
        return ApiRespone.<DeckResponse>builder()
                .result(deckService.getDeckByLessonId(lessonId))
                .code(1000)
                .message("success")
                .build();
    }
}
