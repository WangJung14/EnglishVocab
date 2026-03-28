package trung.supper.englishgrammar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.FlashcardRequest;
import trung.supper.englishgrammar.dto.request.ReorderFlashcardRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.FlashcardResponse;
import trung.supper.englishgrammar.sercurity.CustomUserDetails;
import trung.supper.englishgrammar.services.IFlashcardService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/flashcard-decks/{deckId}/cards")
@RequiredArgsConstructor
public class FlashcardController {

    private final IFlashcardService flashcardService;

    @GetMapping
    public ApiRespone<List<FlashcardResponse>> getCards(@PathVariable UUID deckId) {
        return ApiRespone.<List<FlashcardResponse>>builder()
                .result(flashcardService.getCardsByDeckId(deckId))
                .code(1000)
                .message("success")
                .build();
    }

    @PostMapping
    public ApiRespone<FlashcardResponse> addCard(
            @PathVariable UUID deckId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody FlashcardRequest request) {
        return ApiRespone.<FlashcardResponse>builder()
                .result(flashcardService.addCard(deckId, userDetails.getUser().getId(), request))
                .code(1000)
                .message("Successfully added card")
                .build();
    }

    @PutMapping("/{cardId}")
    public ApiRespone<FlashcardResponse> updateCard(
            @PathVariable UUID deckId,
            @PathVariable UUID cardId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody FlashcardRequest request) {
        return ApiRespone.<FlashcardResponse>builder()
                .result(flashcardService.updateCard(deckId, cardId, userDetails.getUser().getId(), request))
                .code(1000)
                .message("Successfully updated card")
                .build();
    }

    @DeleteMapping("/{cardId}")
    public ApiRespone<Void> deleteCard(
            @PathVariable UUID deckId,
            @PathVariable UUID cardId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        flashcardService.deleteCard(deckId, cardId, userDetails.getUser().getId());
        return ApiRespone.<Void>builder()
                .code(1000)
                .message("Successfully deleted card")
                .build();
    }

    @PutMapping("/reorder")
    public ApiRespone<List<FlashcardResponse>> reorderCards(
            @PathVariable UUID deckId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ReorderFlashcardRequest request) {
        return ApiRespone.<List<FlashcardResponse>>builder()
                .result(flashcardService.reorderCards(deckId, userDetails.getUser().getId(), request))
                .code(1000)
                .message("Successfully reordered cards")
                .build();
    }
}
