package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.UserAnswerLog;

import java.util.List;
import java.util.UUID;

@Repository
public interface IUserAnswerLogRepository extends JpaRepository<UserAnswerLog, UUID> {
    List<UserAnswerLog> findByAttemptId(UUID attemptId);
}
