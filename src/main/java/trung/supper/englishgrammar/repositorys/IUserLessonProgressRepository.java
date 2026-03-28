package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.enums.ProgressStatus;
import trung.supper.englishgrammar.models.UserLessonProgress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserLessonProgressRepository extends JpaRepository<UserLessonProgress, UUID> {

    Optional<UserLessonProgress> findByUserIdAndLessonId(UUID userId, UUID lessonId);

    @Query("SELECT p FROM UserLessonProgress p JOIN FETCH p.lesson WHERE p.user.id = :userId")
    List<UserLessonProgress> findAllByUserId(@Param("userId") UUID userId);

    @Query("SELECT p FROM UserLessonProgress p JOIN FETCH p.lesson l WHERE p.user.id = :userId AND l.topic.id = :topicId")
    List<UserLessonProgress> findByUserIdAndTopicId(@Param("userId") UUID userId, @Param("topicId") UUID topicId);

    @Query("SELECT COUNT(l) FROM Lesson l WHERE l.topic.id = :topicId")
    long countTotalLessonsByTopicId(@Param("topicId") UUID topicId);

    @Query("SELECT COUNT(p) FROM UserLessonProgress p WHERE p.user.id = :userId AND p.lesson.topic.id = :topicId AND p.status = :status")
    long countByUserIdAndTopicIdAndStatus(@Param("userId") UUID userId, @Param("topicId") UUID topicId, @Param("status") ProgressStatus status);

    @Query("SELECT COUNT(p) FROM UserLessonProgress p WHERE p.user.id = :userId AND p.status = :status")
    long countByUserIdAndStatus(@Param("userId") UUID userId, @Param("status") ProgressStatus status);

    @Query("SELECT COUNT(DISTINCT l.topic.id) FROM UserLessonProgress p JOIN p.lesson l WHERE p.user.id = :userId AND p.status = :status")
    long countDistinctCompletedTopicsByUserId(@Param("userId") UUID userId, @Param("status") ProgressStatus status);

    @Query("SELECT p.completedAt FROM UserLessonProgress p WHERE p.user.id = :userId AND p.status = :status AND p.completedAt IS NOT NULL ORDER BY p.completedAt ASC")
    List<LocalDateTime> findCompletedAtByUserId(@Param("userId") UUID userId, @Param("status") ProgressStatus status);
}
