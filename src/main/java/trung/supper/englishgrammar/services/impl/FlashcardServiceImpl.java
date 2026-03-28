package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.FlashcardRequest;
import trung.supper.englishgrammar.dto.request.ReorderFlashcardRequest;
import trung.supper.englishgrammar.dto.response.FlashcardResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.mapper.FlashcardMapper;
import trung.supper.englishgrammar.models.Flashcard;
import trung.supper.englishgrammar.models.FlashcardDeck;
import trung.supper.englishgrammar.repositorys.IFlashcardDeckRepository;
import trung.supper.englishgrammar.repositorys.IFlashcardRepository;
import trung.supper.englishgrammar.services.IFlashcardService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlashcardServiceImpl implements IFlashcardService {

    private final IFlashcardRepository flashcardRepository;
    private final IFlashcardDeckRepository deckRepository;
    private final FlashcardMapper flashcardMapper;

    @Override
    @Transactional(readOnly = true)
    public List<FlashcardResponse> getCardsByDeckId(UUID deckId) {
        validateDeckExists(deckId);
        return flashcardRepository.findByDeckIdOrderByOrderIndexAsc(deckId)
                .stream().map(flashcardMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public FlashcardResponse addCard(UUID deckId, UUID userId, FlashcardRequest request) {
        FlashcardDeck deck = validateDeckOwnership(deckId, userId);

        Integer currentMax = flashcardRepository.findMaxOrderIndexByDeckId(deckId);
        int nextOrder = (currentMax != null ? currentMax : 0) + 1;

        Flashcard card = flashcardMapper.toEntity(request);
        card.setDeck(deck);
        card.setOrderIndex(nextOrder);

        Flashcard saved = flashcardRepository.save(card);
        return flashcardMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public FlashcardResponse updateCard(UUID deckId, UUID cardId, UUID userId, FlashcardRequest request) {
        validateDeckOwnership(deckId, userId);

        Flashcard card = flashcardRepository.findById(cardId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!card.getDeck().getId().equals(deckId)) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        flashcardMapper.updateEntity(card, request);
        Flashcard updated = flashcardRepository.save(card);
        return flashcardMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteCard(UUID deckId, UUID cardId, UUID userId) {
        validateDeckOwnership(deckId, userId);

        Flashcard card = flashcardRepository.findById(cardId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!card.getDeck().getId().equals(deckId)) {
            throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
        }

        flashcardRepository.delete(card);
    }

    @Override
    @Transactional
    public List<FlashcardResponse> reorderCards(UUID deckId, UUID userId, ReorderFlashcardRequest request) {
        validateDeckOwnership(deckId, userId);

        List<UUID> newOrder = request.getCardIds();
        List<Flashcard> existing = flashcardRepository.findByDeckIdOrderByOrderIndexAsc(deckId);

        if (existing.size() != newOrder.size()) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        Map<UUID, Flashcard> cardMap = existing.stream()
                .collect(Collectors.toMap(Flashcard::getId, c -> c));

        List<Flashcard> updated = new ArrayList<>();
        int index = 1;

        for (UUID cardId : newOrder) {
            Flashcard card = cardMap.get(cardId);
            if (card == null) {
                throw new AppException(ErrorCode.RESOURCE_NOT_FOUND);
            }
            card.setOrderIndex(index++);
            updated.add(flashcardRepository.save(card));
        }

        return updated.stream().map(flashcardMapper::toResponse).collect(Collectors.toList());
    }

    private FlashcardDeck validateDeckExists(UUID deckId) {
        return deckRepository.findById(deckId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
    }

    private FlashcardDeck validateDeckOwnership(UUID deckId, UUID userId) {
        FlashcardDeck deck = validateDeckExists(deckId);
        if (!deck.getCreatedBy().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return deck;
    }
}
