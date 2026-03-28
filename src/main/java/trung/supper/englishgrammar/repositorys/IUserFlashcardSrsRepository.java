package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.UserFlashcardSrs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserFlashcardSrsRepository extends JpaRepository<UserFlashcardSrs, UUID> {

    Optional<UserFlashcardSrs> findByUserIdAndFlashcardId(UUID userId, UUID flashcardId);

    @Query("SELECT s FROM UserFlashcardSrs s " +
           "JOIN FETCH s.flashcard f " +
           "WHERE s.user.id = :userId AND s.nextReviewAt <= :now")
    List<UserFlashcardSrs> findDueCards(@Param("userId") UUID userId, @Param("now") LocalDateTime now);

    @Query("SELECT s FROM UserFlashcardSrs s " +
           "JOIN FETCH s.flashcard f " +
           "WHERE s.user.id = :userId AND f.deck.id = :deckId AND s.nextReviewAt <= :now")
    List<UserFlashcardSrs> findDueCardsByDeckId(@Param("userId") UUID userId,
                                                 @Param("deckId") UUID deckId,
                                                 @Param("now") LocalDateTime now);

    long countByUserId(UUID userId);

    long countByUserIdAndNextReviewAtBefore(UUID userId, LocalDateTime now);

    @Query("SELECT COUNT(s) FROM UserFlashcardSrs s WHERE s.user.id = :userId AND s.repetition >= 3")
    long countLearnedByUserId(@Param("userId") UUID userId);
}
