package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import trung.supper.englishgrammar.dto.request.ReviewRequest;
import trung.supper.englishgrammar.dto.response.FlashcardDueResponse;
import trung.supper.englishgrammar.dto.response.SrsStatsResponse;
import trung.supper.englishgrammar.enums.ErrorCode;
import trung.supper.englishgrammar.exception.AppException;
import trung.supper.englishgrammar.models.Flashcard;
import trung.supper.englishgrammar.models.User;
import trung.supper.englishgrammar.models.UserFlashcardSrs;
import trung.supper.englishgrammar.repositorys.IFlashcardRepository;
import trung.supper.englishgrammar.repositorys.IUserFlashcardSrsRepository;
import trung.supper.englishgrammar.services.ISrsService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SrsServiceImpl implements ISrsService {

    private final IUserFlashcardSrsRepository srsRepository;
    private final IFlashcardRepository flashcardRepository;

    // ─── Due Cards ───

    @Override
    @Transactional(readOnly = true)
    public List<FlashcardDueResponse> getDueCards(UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        return srsRepository.findDueCards(userId, now)
                .stream().map(this::toFlashcardDueResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlashcardDueResponse> getDueCardsByDeckId(UUID userId, UUID deckId) {
        LocalDateTime now = LocalDateTime.now();
        return srsRepository.findDueCardsByDeckId(userId, deckId, now)
                .stream().map(this::toFlashcardDueResponse).collect(Collectors.toList());
    }

    // ─── Review ───

    @Override
    @Transactional
    public FlashcardDueResponse reviewCard(UUID userId, UUID flashcardId, ReviewRequest request) {
        Flashcard flashcard = flashcardRepository.findById(flashcardId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        // Get or create SRS progress for this user+flashcard pair
        UserFlashcardSrs progress = srsRepository.findByUserIdAndFlashcardId(userId, flashcardId)
                .orElseGet(() -> createInitialProgress(userId, flashcard));

        // Apply SM-2 algorithm
        applySm2Algorithm(progress, request.getQuality());

        // Update review metadata
        progress.setLastReviewedAt(LocalDateTime.now());
        progress.setTotalReviews(progress.getTotalReviews() + 1);

        UserFlashcardSrs saved = srsRepository.save(progress);
        return toFlashcardDueResponse(saved);
    }

    // ─── Stats ───

    @Override
    @Transactional(readOnly = true)
    public SrsStatsResponse getStats(UUID userId) {
        LocalDateTime now = LocalDateTime.now();
        long totalCards = srsRepository.countByUserId(userId);
        long dueCards = srsRepository.countByUserIdAndNextReviewAtBefore(userId, now);
        long learnedCards = srsRepository.countLearnedByUserId(userId);

        return SrsStatsResponse.builder()
                .totalCards(totalCards)
                .dueCards(dueCards)
                .learnedCards(learnedCards)
                .build();
    }

    // ═══════════════════════════════════════════
    // SM-2 Algorithm — isolated for Strategy Pattern readiness
    // ═══════════════════════════════════════════

    /**
     * Applies the SM-2 Spaced Repetition algorithm.
     *
     * @param progress the user's SRS progress for a specific flashcard
     * @param quality  the quality rating (0-5) from the review
     *
     * This method is intentionally package-visible and self-contained
     * so it can be extracted into a ScoringStrategy interface later.
     */
    void applySm2Algorithm(UserFlashcardSrs progress, int quality) {
        int repetition = progress.getRepetition();
        int interval = progress.getIntervalDays();
        double easeFactor = progress.getEaseFactor();

        if (quality < 3) {
            // Failed — reset
            repetition = 0;
            interval = 1;
        } else {
            // Passed
            repetition += 1;

            if (repetition == 1) {
                interval = 1;
            } else if (repetition == 2) {
                interval = 6;
            } else {
                interval = (int) Math.round(interval * easeFactor);
            }
        }

        // Update ease factor (SM-2 formula)
        easeFactor = easeFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02));
        if (easeFactor < 1.3) {
            easeFactor = 1.3;
        }

        // Calculate next review date
        LocalDateTime nextReview = LocalDateTime.now().plusDays(interval);

        // Persist changes
        progress.setRepetition(repetition);
        progress.setIntervalDays(interval);
        progress.setEaseFactor(easeFactor);
        progress.setNextReviewAt(nextReview);
    }

    // ─── Helpers ───

    private UserFlashcardSrs createInitialProgress(UUID userId, Flashcard flashcard) {
        User userRef = new User();
        userRef.setId(userId);

        return UserFlashcardSrs.builder()
                .user(userRef)
                .flashcard(flashcard)
                .repetition(0)
                .intervalDays(0)
                .easeFactor(2.5)
                .nextReviewAt(LocalDateTime.now())
                .totalReviews(0)
                .build();
    }

    private FlashcardDueResponse toFlashcardDueResponse(UserFlashcardSrs srs) {
        Flashcard card = srs.getFlashcard();
        return FlashcardDueResponse.builder()
                .flashcardId(card.getId())
                .frontText(card.getFront())
                .backText(card.getBack())
                .nextReviewDate(srs.getNextReviewAt())
                .build();
    }
}
