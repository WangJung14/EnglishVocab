package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.User;

import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}
