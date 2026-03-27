package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.enums.Level;
import trung.supper.englishgrammar.models.Topic;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ITopicRepository extends JpaRepository<Topic, UUID> {
    
    Optional<Topic> findBySlug(String slug);
    
    List<Topic> findByCategory_SlugOrderByOrderIndexAsc(String categorySlug);

    @Query("SELECT t FROM Topic t WHERE " +
           "(:categorySlug IS NULL OR t.category.slug = :categorySlug) AND " +
           "(:level IS NULL OR t.level = :level) " +
           "ORDER BY t.orderIndex ASC")
    List<Topic> findTopicsByFilter(@Param("categorySlug") String categorySlug, 
                                   @Param("level") Level level);
}
