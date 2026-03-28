package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.FlashcardRequest;
import trung.supper.englishgrammar.dto.request.ReorderFlashcardRequest;
import trung.supper.englishgrammar.dto.response.FlashcardResponse;

import java.util.List;
import java.util.UUID;

public interface IFlashcardService {
    List<FlashcardResponse> getCardsByDeckId(UUID deckId);
    FlashcardResponse addCard(UUID deckId, UUID userId, FlashcardRequest request);
    FlashcardResponse updateCard(UUID deckId, UUID cardId, UUID userId, FlashcardRequest request);
    void deleteCard(UUID deckId, UUID cardId, UUID userId);
    List<FlashcardResponse> reorderCards(UUID deckId, UUID userId, ReorderFlashcardRequest request);
}
