package trung.supper.englishgrammar.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import trung.supper.englishgrammar.enums.CefrLevel;
import trung.supper.englishgrammar.enums.PartOfSpeech;
import trung.supper.englishgrammar.models.Word;

import java.util.List;
import java.util.UUID;

@Repository
public interface IWordRepository extends JpaRepository<Word, UUID> {

    @Query("SELECT w FROM Word w WHERE " +
           "(:level IS NULL OR w.level = :level) AND " +
           "(:partOfSpeech IS NULL OR w.partOfSpeech = :partOfSpeech) " +
           "ORDER BY w.createdAt DESC")
    List<Word> findWordsByFilter(@Param("level") CefrLevel level, 
                                 @Param("partOfSpeech") PartOfSpeech partOfSpeech);
}
