package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.UserStreak;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserStreakRepository extends JpaRepository<UserStreak, UUID> {
    Optional<UserStreak> findByUserId(UUID userId);
}
