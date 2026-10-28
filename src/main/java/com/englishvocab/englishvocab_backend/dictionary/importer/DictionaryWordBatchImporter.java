package com.englishvocab.englishvocab_backend.dictionary.importer;

import org.sqlite.SQLiteConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(
        prefix = "app.import",
        name = "words-enabled",
        havingValue = "true"
)
public class DictionaryWordBatchImporter implements ApplicationRunner {

    private static final int BATCH_SIZE = 100;

    private final String sqlitePath;
    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public DictionaryWordBatchImporter(
            @Value("${app.import.sqlite-path}") String sqlitePath,
            JdbcTemplate jdbcTemplate,
            TransactionTemplate transactionTemplate
    ) {
        this.sqlitePath = sqlitePath;
        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        System.out.println("===== START WORD IMPORT =====");

        List<WordImportRow> words = readWordsFromSQLite();

        System.out.println(
                "Loaded from SQLite: " + words.size()
        );

        int inserted = transactionTemplate.execute(status ->
                insertWordsIntoPostgres(words)
        );

        System.out.println("Inserted words: " + inserted);
        System.out.println("===== WORD IMPORT COMPLETED =====");
    }

    private List<WordImportRow> readWordsFromSQLite()
            throws Exception {

        Path path = Path.of(sqlitePath).toAbsolutePath();

        if (!Files.isRegularFile(path)) {
            throw new IllegalStateException(
                    "SQLite file not found: " + path
            );
        }

        List<WordImportRow> words = new ArrayList<>();

        SQLiteConfig config = new SQLiteConfig();
        config.setReadOnly(true);

        String url = "jdbc:sqlite:" + path;

        String sql = """
                SELECT id, word, lang_code
                FROM words
                WHERE word IS NOT NULL
                  AND length(trim(word)) > 0
                  AND length(trim(coalesce(lang_code, 'en')))
                      BETWEEN 1 AND 10
                ORDER BY id
                LIMIT ?
                """;

        try (Connection connection =
                     DriverManager.getConnection(
                             url, config.toProperties()
                     )) {

            try (Statement statement =
                         connection.createStatement()) {
                statement.execute("PRAGMA query_only = ON");
            }

            try (PreparedStatement statement =
                         connection.prepareStatement(sql)) {

                statement.setInt(1, BATCH_SIZE);

                try (ResultSet rs = statement.executeQuery()) {

                    while (rs.next()) {

                        long sourceId = rs.getLong("id");
                        String word = rs.getString("word");
                        String language = rs.getString("lang_code");

                        if (language == null) {
                            language = "en";
                        }

                        words.add(new WordImportRow(
                                sourceId,
                                word,
                                language
                        ));
                    }
                }
            }
        }

        return words;
    }

    private int insertWordsIntoPostgres(
            List<WordImportRow> words
    ) {

        String sql = """
                INSERT INTO dictionary_words (
                    source_word_id,
                    word,
                    language_code
                )
                VALUES (?, ?, ?)
                ON CONFLICT (source_word_id) DO NOTHING
                """;

        int[][] results = jdbcTemplate.batchUpdate(
                sql,
                words,
                BATCH_SIZE,
                (ps, word) -> {
                    ps.setLong(1, word.sourceWordId());
                    ps.setString(2, word.word());
                    ps.setString(3, word.languageCode());
                }
        );

        return Arrays.stream(results)
                .flatMapToInt(Arrays::stream)
                .filter(count -> count > 0)
                .sum();
    }
}
