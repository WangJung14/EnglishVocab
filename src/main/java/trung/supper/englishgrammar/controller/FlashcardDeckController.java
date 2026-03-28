package trung.supper.englishgrammar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.DeckRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.DeckResponse;
import trung.supper.englishgrammar.sercurity.CustomUserDetails;
import trung.supper.englishgrammar.services.IFlashcardDeckService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/flashcard-decks")
@RequiredArgsConstructor
public class FlashcardDeckController {

    private final IFlashcardDeckService deckService;

    @GetMapping
    public ApiRespone<List<DeckResponse>> getDecks(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<List<DeckResponse>>builder()
                .result(deckService.getVisibleDecks(userDetails.getUser().getId()))
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/{id}")
    public ApiRespone<DeckResponse> getDeckById(@PathVariable UUID id) {
        return ApiRespone.<DeckResponse>builder()
                .result(deckService.getDeckById(id))
                .code(1000)
                .message("success")
                .build();
    }

    @PostMapping
    public ApiRespone<DeckResponse> createDeck(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody DeckRequest request) {
        return ApiRespone.<DeckResponse>builder()
                .result(deckService.createDeck(userDetails.getUser().getId(), request))
                .code(1000)
                .message("Successfully created flashcard deck")
                .build();
    }

    @PutMapping("/{id}")
    public ApiRespone<DeckResponse> updateDeck(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody DeckRequest request) {
        return ApiRespone.<DeckResponse>builder()
                .result(deckService.updateDeck(id, userDetails.getUser().getId(), request))
                .code(1000)
                .message("Successfully updated flashcard deck")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiRespone<Void> deleteDeck(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        deckService.deleteDeck(id, userDetails.getUser().getId());
        return ApiRespone.<Void>builder()
                .code(1000)
                .message("Successfully deleted flashcard deck")
                .build();
    }
}
