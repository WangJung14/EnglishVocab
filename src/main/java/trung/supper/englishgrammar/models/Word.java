package trung.supper.englishgrammar.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import trung.supper.englishgrammar.enums.CefrLevel;
import trung.supper.englishgrammar.enums.PartOfSpeech;

@Entity
@Table(name = "words")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter

public class Word {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String word;

    @Column(length = 100)
    private String phonetic; // phien am

    @Column(name = "audio_url", columnDefinition = "TEXT")
    private String audioUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "part_of_speech")
    private PartOfSpeech partOfSpeech;

    @Column(name = "definition_vi", columnDefinition = "TEXT")
    private String definitionVi;

    @Column(name = "example_sentence", columnDefinition = "TEXT")
    private String exampleSentence;

    @Column(name = "example_transaction", columnDefinition = "TEXT")
    private String exampleTransaction;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private CefrLevel level;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
