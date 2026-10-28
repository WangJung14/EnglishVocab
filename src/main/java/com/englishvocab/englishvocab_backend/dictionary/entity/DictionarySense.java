package com.englishvocab.englishvocab_backend.dictionary.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "dictionary_senses")
public class DictionarySense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "word_id",nullable = false)
    private DictionaryWord word;

    @Column(name = "meaning_vi",nullable = false)
    private String meaningVi;

    @Column(name = "part_of_speech_raw",length = 100)
    private String partOfSpeechRaw;

    @Column(name = "example")
    private String example;

    @Column(name =  "source_link_id",unique = true)
    private long sourceLinkId;

}
