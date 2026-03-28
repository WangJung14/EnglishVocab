package trung.supper.englishgrammar.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import trung.supper.englishgrammar.dto.request.ReviewRequest;
import trung.supper.englishgrammar.dto.response.ApiRespone;
import trung.supper.englishgrammar.dto.response.FlashcardDueResponse;
import trung.supper.englishgrammar.dto.response.SrsStatsResponse;
import trung.supper.englishgrammar.sercurity.CustomUserDetails;
import trung.supper.englishgrammar.services.ISrsService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/srs")
@RequiredArgsConstructor
public class SrsController {

    private final ISrsService srsService;

    @GetMapping("/due")
    public ApiRespone<List<FlashcardDueResponse>> getDueCards(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<List<FlashcardDueResponse>>builder()
                .result(srsService.getDueCards(userDetails.getUser().getId()))
                .code(1000)
                .message("success")
                .build();
    }

    @GetMapping("/decks/{deckId}/due")
    public ApiRespone<List<FlashcardDueResponse>> getDueCardsByDeck(
            @PathVariable UUID deckId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<List<FlashcardDueResponse>>builder()
                .result(srsService.getDueCardsByDeckId(userDetails.getUser().getId(), deckId))
                .code(1000)
                .message("success")
                .build();
    }

    @PostMapping("/review/{flashcardId}")
    public ApiRespone<FlashcardDueResponse> reviewCard(
            @PathVariable UUID flashcardId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ReviewRequest request) {
        return ApiRespone.<FlashcardDueResponse>builder()
                .result(srsService.reviewCard(userDetails.getUser().getId(), flashcardId, request))
                .code(1000)
                .message("Review recorded")
                .build();
    }

    @GetMapping("/stats")
    public ApiRespone<SrsStatsResponse> getStats(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiRespone.<SrsStatsResponse>builder()
                .result(srsService.getStats(userDetails.getUser().getId()))
                .code(1000)
                .message("success")
                .build();
    }
}
