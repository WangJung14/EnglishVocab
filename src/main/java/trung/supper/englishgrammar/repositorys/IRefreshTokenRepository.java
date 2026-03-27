package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import trung.supper.englishgrammar.models.RefreshToken;

import java.util.Optional;

public interface IRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(trung.supper.englishgrammar.models.User user);
}
