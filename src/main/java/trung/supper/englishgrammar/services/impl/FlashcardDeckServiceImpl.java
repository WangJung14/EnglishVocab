package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.DeckRequest;
import trung.supper.englishgrammar.dto.response.DeckResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.mapper.FlashcardDeckMapper;
import trung.supper.englishgrammar.models.FlashcardDeck;
import trung.supper.englishgrammar.models.User;
import trung.supper.englishgrammar.repositorys.IFlashcardDeckRepository;
import trung.supper.englishgrammar.services.IFlashcardDeckService;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlashcardDeckServiceImpl implements IFlashcardDeckService {

    private final IFlashcardDeckRepository deckRepository;
    private final FlashcardDeckMapper deckMapper;

    @Override
    @Transactional(readOnly = true)
    public List<DeckResponse> getVisibleDecks(UUID userId) {
        return deckRepository.findVisibleDecks(userId)
                .stream().map(deckMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public DeckResponse getDeckById(UUID deckId) {
        FlashcardDeck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return deckMapper.toResponse(deck);
    }

    @Override
    @Transactional(readOnly = true)
    public DeckResponse getDeckByLessonId(UUID lessonId) {
        FlashcardDeck deck = deckRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return deckMapper.toResponse(deck);
    }

    @Override
    @Transactional
    public DeckResponse createDeck(UUID userId, DeckRequest request) {
        FlashcardDeck newDeck = deckMapper.toEntity(request);

        User owner = new User();
        owner.setId(userId);
        newDeck.setCreatedBy(owner);

        if (newDeck.getIsPublic() == null) {
            newDeck.setIsPublic(false);
        }

        FlashcardDeck saved = deckRepository.save(newDeck);
        return deckMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public DeckResponse updateDeck(UUID deckId, UUID userId, DeckRequest request) {
        FlashcardDeck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        validateOwnership(deck, userId);

        deckMapper.updateEntity(deck, request);
        FlashcardDeck updated = deckRepository.save(deck);
        return deckMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteDeck(UUID deckId, UUID userId) {
        FlashcardDeck deck = deckRepository.findById(deckId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        validateOwnership(deck, userId);
        deckRepository.delete(deck);
    }

    /**
     * Ownership check — enforced at service level per requirements.
     * Only the creator can modify/delete their deck.
     */
    private void validateOwnership(FlashcardDeck deck, UUID userId) {
        if (!deck.getCreatedBy().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }
}
