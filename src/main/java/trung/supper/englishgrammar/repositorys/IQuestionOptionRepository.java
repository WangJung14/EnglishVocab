package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.QuestionOption;

import java.util.List;
import java.util.UUID;

@Repository
public interface IQuestionOptionRepository extends JpaRepository<QuestionOption, UUID> {

    List<QuestionOption> findByQuestionIdOrderByOrderIndexAsc(UUID questionId);

    boolean existsByQuestionIdAndContent(UUID questionId, String content);

    @Query("SELECT MAX(o.orderIndex) FROM QuestionOption o WHERE o.question.id = :questionId")
    Integer findMaxOrderIndexByQuestionId(@Param("questionId") UUID questionId);
}
