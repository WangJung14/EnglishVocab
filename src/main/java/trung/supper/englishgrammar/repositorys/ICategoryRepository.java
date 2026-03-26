package trung.supper.englishgrammar.repositorys;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import trung.supper.englishgrammar.models.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, UUID> {

}
