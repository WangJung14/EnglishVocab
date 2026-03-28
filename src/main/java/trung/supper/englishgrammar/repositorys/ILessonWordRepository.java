package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.LessonWord;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ILessonWordRepository extends JpaRepository<LessonWord, UUID> {
    
    List<LessonWord> findByLessonIdOrderByOrderIndexAsc(UUID lessonId);
    
    boolean existsByLessonIdAndWordId(UUID lessonId, UUID wordId);

    Optional<LessonWord> findByLessonIdAndWordId(UUID lessonId, UUID wordId);

    @Query("SELECT MAX(lw.orderIndex) FROM LessonWord lw WHERE lw.lesson.id = :lessonId")
    Integer findMaxOrderIndexByLessonId(@Param("lessonId") UUID lessonId);

    @Modifying
    @Query("UPDATE LessonWord lw SET lw.orderIndex = lw.orderIndex - 1 WHERE lw.lesson.id = :lessonId AND lw.orderIndex > :orderIndex")
    void decrementOrderIndexAfter(@Param("lessonId") UUID lessonId, @Param("orderIndex") Integer orderIndex);
}
