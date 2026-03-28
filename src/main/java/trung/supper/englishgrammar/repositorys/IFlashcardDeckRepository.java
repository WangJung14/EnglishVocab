package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.FlashcardDeck;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IFlashcardDeckRepository extends JpaRepository<FlashcardDeck, UUID> {

    @Query("SELECT d FROM FlashcardDeck d WHERE d.isPublic = true OR d.createdBy.id = :userId ORDER BY d.createdAt DESC")
    List<FlashcardDeck> findVisibleDecks(@Param("userId") UUID userId);

    Optional<FlashcardDeck> findByLessonId(UUID lessonId);
}
