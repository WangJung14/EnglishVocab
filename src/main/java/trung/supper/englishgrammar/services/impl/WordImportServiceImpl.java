package trung.supper.englishgrammar.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import trung.supper.englishgrammar.dto.request.WordRequest;
import trung.supper.englishgrammar.dto.response.BulkImportResponse;
import trung.supper.englishgrammar.enums.CefrLevel;
import trung.supper.englishgrammar.enums.PartOfSpeech;
import trung.supper.englishgrammar.services.IWordImportService;
import trung.supper.englishgrammar.services.IWordService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WordImportServiceImpl implements IWordImportService {

    private final IWordService wordService;

    // Headings for template
    private static final String[] HEADERS = {"word", "meaning", "pronunciation", "example", "level", "partOfSpeech"};

    @Override
    public BulkImportResponse importWords(MultipartFile file) {
        int total = 0;
        int success = 0;
        int failed = 0;
        List<String> errors = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.builder()
                             .setHeader()
                             .setSkipHeaderRecord(true)
                             .setIgnoreHeaderCase(true)
                             .setTrim(true)
                             .build())) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                total++;
                try {
                    WordRequest request = new WordRequest();
                    request.setWord(csvRecord.get("word"));
                    request.setMeaning(csvRecord.get("meaning"));
                    request.setPronunciation(csvRecord.get("pronunciation"));
                    request.setExample(csvRecord.get("example"));

                    // parse enums
                    String levelStr = csvRecord.get("level").toUpperCase();
                    request.setLevel(CefrLevel.valueOf(levelStr));

                    String posStr = csvRecord.get("partOfSpeech").toUpperCase();
                    request.setPartOfSpeech(PartOfSpeech.valueOf(posStr));

                    // Add validation manually or rely on GlobalExceptionHandler/validation interceptors in service
                    // To keep it simple, calling wordService will execute the DB part. We're directly bypassing spring @Valid since we programmatically construct the DTO.
                    if (request.getWord() == null || request.getWord().isBlank() || request.getMeaning() == null || request.getMeaning().isBlank()) {
                        throw new IllegalArgumentException("Word and Meaning cannot be empty");
                    }

                    wordService.createWord(request);
                    success++;
                } catch (IllegalArgumentException e) {
                    failed++;
                    errors.add("Row " + total + " failed: Invalid Enum value or missing fields (" + e.getMessage() + ")");
                } catch (Exception e) {
                    failed++;
                    errors.add("Row " + total + " failed: " + e.getMessage());
                }
            }

        } catch (Exception e) {
            errors.add("File parsing failed: " + e.getMessage());
        }

        return BulkImportResponse.builder()
                .total(total)
                .success(success)
                .failed(failed)
                .errors(errors)
                .build();
    }

    @Override
    public byte[] generateTemplate() {
        StringBuilder builder = new StringBuilder();
        // optionally add UTF-8 BOM
        builder.append('\ufeff');
        builder.append(String.join(",", HEADERS)).append("\n");
        // add a sample row
        builder.append("hello,xin chào,/heˈloʊ/,Hello world,A1,NOUN").append("\n");
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
