package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.ReadingPassage;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IReadingPassageRepository extends JpaRepository<ReadingPassage, UUID> {
    Optional<ReadingPassage> findByLessonId(UUID lessonId);
    boolean existsByLessonId(UUID lessonId);
}
