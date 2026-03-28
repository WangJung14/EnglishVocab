package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.Lesson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ILessonRepository extends JpaRepository<Lesson, UUID> {
    Optional<Lesson> findByIdAndTopicId(UUID lessonId, UUID topicId);
    List<Lesson> findByTopicIdOrderByOrderIndexAsc(UUID topicId);
    List<Lesson> findByTopic_SlugOrderByOrderIndexAsc(String topicSlug);
    Optional<Lesson> findBySlug(String slug);

    // Top started lessons: returns [lessonId, progressCount]
    @Query(value = "SELECT p.lesson_id, COUNT(p.id) AS progressCount " +
                   "FROM user_lesson_progress p " +
                   "GROUP BY p.lesson_id " +
                   "ORDER BY progressCount DESC " +
                   "LIMIT :limit", nativeQuery = true)
    List<Object[]> findMostPopularLessons(@Param("limit") int limit);
}
