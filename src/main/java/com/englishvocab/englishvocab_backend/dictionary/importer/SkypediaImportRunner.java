package com.englishvocab.englishvocab_backend.dictionary.importer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(
        prefix = "app.import",
        name = "full-enabled",
        havingValue = "true"
)
public class SkypediaImportRunner implements ApplicationRunner {

    private final SkypediaImportService importService;

    public SkypediaImportRunner(
            SkypediaImportService importService
    ) {
        this.importService = importService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        importService.importAll();
    }
}
