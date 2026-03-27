package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.Lesson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ILessonRepository extends JpaRepository<Lesson, UUID> {
    Optional<Lesson> findBySlug(String slug);
    List<Lesson> findByTopic_SlugOrderByOrderIndexAsc(String topicSlug);
}
