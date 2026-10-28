package com.englishvocab.englishvocab_backend.dictionary.importer;

import org.sqlite.SQLiteConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@Service
public class SkypediaImportService {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;
    private final String sqlitePath;
    private final int batchSize;

    public SkypediaImportService(
            JdbcTemplate jdbcTemplate,
            TransactionTemplate transactionTemplate,
            @Value("${app.import.sqlite-path:}") String sqlitePath,
            @Value("${app.import.batch-size:500}") int batchSize
    ) {
        if (batchSize < 1 || batchSize > 5000) {
            throw new IllegalArgumentException(
                    "batch-size must be between 1 and 5000"
            );
        }

        this.jdbcTemplate = jdbcTemplate;
        this.transactionTemplate = transactionTemplate;
        this.sqlitePath = sqlitePath;
        this.batchSize = batchSize;
    }

    public void importAll() throws Exception {

        Path path = Path.of(sqlitePath).toAbsolutePath();

        if (!Files.isRegularFile(path)) {
            throw new IllegalStateException(
                    "SQLite database not found: " + path
            );
        }

        SQLiteConfig config = new SQLiteConfig();
        config.setReadOnly(true);

        String url = "jdbc:sqlite:" + path;

        try (Connection source = DriverManager.getConnection(
                url, config.toProperties()
        )) {
            try (Statement st = source.createStatement()) {
                st.execute("PRAGMA query_only = ON");
            }

            System.out.println("===== IMPORT STARTED =====");

            importWords(source);
            importSenses(source);
            importPronunciations(source);

            System.out.println("===== IMPORT COMPLETED =====");
        }
    }

    private long countSource(
            Connection source,
            String table
    ) throws SQLException {

        // Only call with hardcoded table names from this class.
        try (Statement st = source.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT COUNT(*) FROM " + table
             )) {
            rs.next();
            return rs.getLong(1);
        }
    }

    private long countTarget(String table) {

        // Only call with hardcoded table names from this class.
        Long count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM " + table,
                Long.class
        );

        return count == null ? 0 : count;
    }

    private void executeBatch(
            String sql,
            List<Object[]> rows
    ) {
        if (rows.isEmpty()) {
            return;
        }

        transactionTemplate.executeWithoutResult(status -> {

            jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {

                        @Override
                        public void setValues(
                                PreparedStatement ps,
                                int index
                        ) throws SQLException {

                            Object[] row = rows.get(index);

                            for (int i = 0; i < row.length; i++) {
                                Object value = row[i];

                                if (value == null) {
                                    ps.setNull(i + 1, Types.NULL);
                                } else {
                                    ps.setObject(i + 1, value);
                                }
                            }
                        }

                        @Override
                        public int getBatchSize() {
                            return rows.size();
                        }
                    }
            );
        });
    }

    // ====================================
    // 1. IMPORT WORDS
    // ====================================

    private void importWords(Connection source)
            throws SQLException {

        ImportStatistics stats =
                new ImportStatistics("WORDS");

        stats.setSourceRows(countSource(source, "words"));
        stats.setDatabaseRowsBefore(
                countTarget("dictionary_words")
        );

        String selectSql = """
                SELECT id, word, lang_code
                FROM words
                WHERE id > ?
                ORDER BY id
                LIMIT ?
                """;

        String insertSql = """
                INSERT INTO dictionary_words (
                    source_word_id,
                    word,
                    language_code
                )
                VALUES (?, ?, ?)
                ON CONFLICT (source_word_id) DO NOTHING
                """;

        long lastId = 0;

        try (PreparedStatement select =
                     source.prepareStatement(selectSql)) {

            while (true) {

                select.setLong(1, lastId);
                select.setInt(2, batchSize);

                List<Object[]> rows = new ArrayList<>();

                int readCount = 0;

                try (ResultSet rs = select.executeQuery()) {

                    while (rs.next()) {
                        readCount++;

                        long id = rs.getLong("id");
                        lastId = id;

                        String word = rs.getString("word");
                        String language =
                                rs.getString("lang_code");

                        if (language == null) {
                            language = "en";
                        }

                        if (isBlank(word)
                                || isBlank(language)
                                || language.length() > 10) {

                            stats.incrementInvalid();
                            continue;
                        }

                        rows.add(new Object[]{
                                id,
                                word,
                                language
                        });

                        stats.incrementAccepted();
                    }
                }

                if (readCount == 0) {
                    break;
                }

                executeBatch(insertSql, rows);
            }
        }

        stats.setDatabaseRowsAfter(
                countTarget("dictionary_words")
        );

        stats.printReport();
    }

    // ====================================
    // 2. IMPORT SENSES
    // ====================================

    private void importSenses(Connection source)
            throws SQLException {

        ImportStatistics stats =
                new ImportStatistics("SENSES");

        long totalLinks =
                countSource(source, "word_definitions");

        stats.setSourceRows(totalLinks);
        stats.setDatabaseRowsBefore(
                countTarget("dictionary_senses")
        );

        String selectSql = """
                SELECT
                    wd.id AS link_id,
                    wd.word_id,
                    wd.definition_id,
                    wd.example,
                    d.definition,
                    d.pos
                FROM word_definitions wd
                LEFT JOIN definitions d
                    ON d.id = wd.definition_id
                WHERE wd.id > ?
                ORDER BY wd.id
                LIMIT ?
                """;

        String insertSql = """
                INSERT INTO dictionary_senses (
                    word_id,
                    meaning_vi,
                    part_of_speech_raw,
                    example,
                    source_definition_id,
                    source_link_id
                )
                SELECT
                    dw.id,
                    ?,
                    ?,
                    ?,
                    ?,
                    ?
                FROM dictionary_words dw
                WHERE dw.source_word_id = ?
                ON CONFLICT (source_link_id) DO NOTHING
                """;

        long lastId = 0;

        try (PreparedStatement select =
                     source.prepareStatement(selectSql)) {

            while (true) {

                select.setLong(1, lastId);
                select.setInt(2, batchSize);

                List<Object[]> rows = new ArrayList<>();
                int readCount = 0;

                try (ResultSet rs = select.executeQuery()) {

                    while (rs.next()) {

                        readCount++;

                        long linkId = rs.getLong("link_id");
                        lastId = linkId;

                        long sourceWordId =
                                rs.getLong("word_id");

                        long definitionId =
                                rs.getLong("definition_id");

                        String meaning =
                                rs.getString("definition");

                        String pos = rs.getString("pos");

                        String example =
                                rs.getString("example");

                        if (isBlank(meaning)) {
                            stats.incrementInvalid();
                            continue;
                        }

                        // Keep the original POS if it fits.
                        // Do not truncate source data.
                        if (pos != null && pos.length() > 100) {
                            stats.incrementInvalid();
                            continue;
                        }

                        rows.add(new Object[]{
                                meaning,
                                pos,
                                example,
                                definitionId,
                                linkId,
                                sourceWordId
                        });

                        stats.incrementAccepted();
                    }
                }

                if (readCount == 0) {
                    break;
                }

                executeBatch(insertSql, rows);
            }
        }

        stats.setDatabaseRowsAfter(
                countTarget("dictionary_senses")
        );

        stats.printReport();

        System.out.println(
                "Note: accepted senses whose source word"
                        + " is absent in PostgreSQL are not inserted."
        );
    }

    // ====================================
    // 3. IMPORT PRONUNCIATIONS
    // ====================================

    private void importPronunciations(Connection source)
            throws SQLException {

        ImportStatistics stats =
                new ImportStatistics("PRONUNCIATIONS");

        stats.setSourceRows(
                countSource(source, "pronunciations")
        );

        stats.setDatabaseRowsBefore(
                countTarget("dictionary_pronunciations")
        );

        String selectSql = """
                SELECT id, word_id, ipa, region
                FROM pronunciations
                WHERE id > ?
                ORDER BY id
                LIMIT ?
                """;

        String insertSql = """
                INSERT INTO dictionary_pronunciations (
                    word_id,
                    ipa,
                    region_raw,
                    source_pronunciation_id
                )
                SELECT
                    dw.id,
                    ?,
                    ?,
                    ?
                FROM dictionary_words dw
                WHERE dw.source_word_id = ?
                ON CONFLICT (source_pronunciation_id) DO NOTHING
                """;

        long lastId = 0;

        try (PreparedStatement select =
                     source.prepareStatement(selectSql)) {

            while (true) {

                select.setLong(1, lastId);
                select.setInt(2, batchSize);

                List<Object[]> rows = new ArrayList<>();
                int readCount = 0;

                try (ResultSet rs = select.executeQuery()) {

                    while (rs.next()) {

                        readCount++;

                        long id = rs.getLong("id");
                        lastId = id;

                        long sourceWordId =
                                rs.getLong("word_id");

                        String ipa = rs.getString("ipa");
                        String region =
                                rs.getString("region");

                        if (isBlank(ipa)) {
                            stats.incrementInvalid();
                            continue;
                        }

                        rows.add(new Object[]{
                                ipa,
                                region,
                                id,
                                sourceWordId
                        });

                        stats.incrementAccepted();
                    }
                }

                if (readCount == 0) {
                    break;
                }

                executeBatch(insertSql, rows);
            }
        }

        stats.setDatabaseRowsAfter(
                countTarget("dictionary_pronunciations")
        );

        stats.printReport();
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
