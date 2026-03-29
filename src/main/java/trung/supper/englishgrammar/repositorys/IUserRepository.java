package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmailIgnoreCase(String email);
    
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByPhoneNumber(String phoneNumber);


    // User growth — group by date string (MySQL: DATE_FORMAT, JPQL-compatible via FUNCTION)
    @Query(value = "SELECT DATE(u.created_at) AS date, COUNT(u.id) AS newUsers " +
                   "FROM users u " +
                   "WHERE u.created_at >= :since " +
                   "GROUP BY DATE(u.created_at) " +
                   "ORDER BY DATE(u.created_at) ASC", nativeQuery = true)
    List<Object[]> findUserGrowthSince(@Param("since") LocalDateTime since);
}
