package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.Exercise;

import java.util.List;
import java.util.UUID;

@Repository
public interface IExerciseRepository extends JpaRepository<Exercise, UUID> {

    List<Exercise> findByLessonIdOrderByOrderIndexAsc(UUID lessonId);

    @Query("SELECT MAX(e.orderIndex) FROM Exercise e WHERE e.lesson.id = :lessonId")
    Integer findMaxOrderIndexByLessonId(@Param("lessonId") UUID lessonId);

    // Top attempted exercises: returns [exerciseId, attemptCount]
    @Query(value = "SELECT a.exercise_id, COUNT(a.id) AS attemptCount " +
                   "FROM user_exercise_attempts a " +
                   "GROUP BY a.exercise_id " +
                   "ORDER BY attemptCount DESC " +
                   "LIMIT :limit", nativeQuery = true)
    List<Object[]> findMostAttemptedExercises(@Param("limit") int limit);
}
