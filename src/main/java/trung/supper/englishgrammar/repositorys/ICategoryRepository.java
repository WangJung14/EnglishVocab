package trung.supper.englishgrammar.repositorys;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import trung.supper.englishgrammar.models.Category;

import java.util.Optional;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, UUID> {
    
    Optional<Category> findBySlug(String slug);
    
    boolean existsBySlug(String slug);
}
