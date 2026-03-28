package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.Flashcard;

import java.util.List;
import java.util.UUID;

@Repository
public interface IFlashcardRepository extends JpaRepository<Flashcard, UUID> {

    List<Flashcard> findByDeckIdOrderByOrderIndexAsc(UUID deckId);

    @Query("SELECT MAX(f.orderIndex) FROM Flashcard f WHERE f.deck.id = :deckId")
    Integer findMaxOrderIndexByDeckId(@Param("deckId") UUID deckId);
}
