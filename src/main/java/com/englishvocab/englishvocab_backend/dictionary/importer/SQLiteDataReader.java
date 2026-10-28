
package com.englishvocab.englishvocab_backend.dictionary.importer;

import org.sqlite.SQLiteConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Component
@ConditionalOnProperty(
        prefix = "app.import",
        name = "preview-enabled",
        havingValue = "true"
)
public class SQLiteDataReader implements ApplicationRunner {

    private final String sqlitePath;

    public SQLiteDataReader(
            @Value("${app.import.sqlite-path}") String sqlitePath
    ) {
        this.sqlitePath = sqlitePath;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        Path path = Path.of(sqlitePath).toAbsolutePath();

        if (!Files.isRegularFile(path)) {
            throw new IllegalStateException(
                    "SQLite file not found: " + path
            );
        }

        SQLiteConfig config = new SQLiteConfig();
        config.setReadOnly(true);

        String url = "jdbc:sqlite:" + path;

        try (Connection connection =
                     DriverManager.getConnection(
                             url,
                             config.toProperties()
                     )) {

            try (Statement statement =
                         connection.createStatement()) {
                statement.execute("PRAGMA query_only = ON");
            }

            System.out.println("===== SQLITE DATASET =====");

            printTableCount(connection, "words");
            printTableCount(connection, "definitions");
            printTableCount(connection, "word_definitions");
            printTableCount(connection, "pronunciations");

            printSampleWords(connection);
        }
    }

    private void printTableCount(
            Connection connection,
            String table
    ) throws Exception {

        String sql = "SELECT COUNT(*) FROM " + table;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {
                System.out.println(
                        table + ": " + rs.getLong(1)
                );
            }
        }
    }

    private void printSampleWords(Connection connection)
            throws Exception {

        String sql = """
                SELECT id, word, lang_code
                FROM words
                ORDER BY id
                LIMIT ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, 10);

            try (ResultSet rs = statement.executeQuery()) {

                System.out.println("===== SAMPLE WORDS =====");

                while (rs.next()) {

                    long id = rs.getLong("id");
                    String word = rs.getString("word");
                    String language = rs.getString("lang_code");

                    System.out.printf(
                            "%d | %s | %s%n",
                            id, word, language
                    );
                }
            }
        }
    }
}
