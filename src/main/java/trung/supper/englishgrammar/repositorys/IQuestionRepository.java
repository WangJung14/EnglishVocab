package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.Question;

import java.util.List;
import java.util.UUID;

@Repository
public interface IQuestionRepository extends JpaRepository<Question, UUID> {

    List<Question> findByExerciseIdOrderByOrderIndexAsc(UUID exerciseId);

    @Query("SELECT MAX(q.orderIndex) FROM Question q WHERE q.exercise.id = :exerciseId")
    Integer findMaxOrderIndexByExerciseId(@Param("exerciseId") UUID exerciseId);
}
