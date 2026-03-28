package trung.supper.englishgrammar.services;

import trung.supper.englishgrammar.dto.request.DeckRequest;
import trung.supper.englishgrammar.dto.response.DeckResponse;

import java.util.List;
import java.util.UUID;

public interface IFlashcardDeckService {
    List<DeckResponse> getVisibleDecks(UUID userId);
    DeckResponse getDeckById(UUID deckId);
    DeckResponse getDeckByLessonId(UUID lessonId);
    DeckResponse createDeck(UUID userId, DeckRequest request);
    DeckResponse updateDeck(UUID deckId, UUID userId, DeckRequest request);
    void deleteDeck(UUID deckId, UUID userId);
}
