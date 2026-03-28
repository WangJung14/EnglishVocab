package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.enums.AttemptStatus;
import trung.supper.englishgrammar.models.UserExerciseAttempt;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserExerciseAttemptRepository extends JpaRepository<UserExerciseAttempt, UUID> {

    List<UserExerciseAttempt> findByExerciseIdAndUserIdOrderByAttemptedAtDesc(UUID exerciseId, UUID userId);

    List<UserExerciseAttempt> findByExerciseIdOrderByAttemptedAtDesc(UUID exerciseId);

    Optional<UserExerciseAttempt> findByIdAndUserId(UUID attemptId, UUID userId);

    Optional<UserExerciseAttempt> findByExerciseIdAndUserIdAndStatus(UUID exerciseId, UUID userId, AttemptStatus status);
}
