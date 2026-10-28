package com.englishvocab.englishvocab_backend.dictionary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "dictionary_pronunciations")
public class DictionaryPronunciation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "word_id", nullable = false)
    private DictionaryWord word;

    @Column(name = "ipa", nullable = false)
    private String ipa;

    @Column(name = "region_raw")
    private String regionRaw;

    @Column(name = "source_pronunciation_id", unique = true)
    private Long sourcePronunciationId;
}
