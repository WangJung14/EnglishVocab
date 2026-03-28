package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.ReviewRequest;
import trung.supper.englishgrammar.dto.response.FlashcardDueResponse;
import trung.supper.englishgrammar.dto.response.SrsStatsResponse;

import java.util.List;
import java.util.UUID;

public interface ISrsService {
    List<FlashcardDueResponse> getDueCards(UUID userId);
    List<FlashcardDueResponse> getDueCardsByDeckId(UUID userId, UUID deckId);
    FlashcardDueResponse reviewCard(UUID userId, UUID flashcardId, ReviewRequest request);
    SrsStatsResponse getStats(UUID userId);
}
