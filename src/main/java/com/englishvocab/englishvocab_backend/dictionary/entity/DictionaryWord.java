package com.englishvocab.englishvocab_backend.dictionary.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name ="dictionary_words")
public class DictionaryWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "language_code",nullable = false, length =  10)
    private String languageCode = "en";

    @Column(name = "source_word_id", unique = true)
    private Long sourceWordId;

    @Column(name = "created_at", nullable = false , insertable = false , updatable = false)
    private OffsetDateTime createdAt;
}
