package com.englishvocab.englishvocab_backend.dictionary.importer;

public class ImportStatistics {

    private final String tableName;

    private long sourceRows;
    private long acceptedRows;
    private long skippedInvalidRows;
    private long skippedMissingReferences;
    private long databaseRowsBefore;
    private long databaseRowsAfter;

    public ImportStatistics(String tableName) {
        this.tableName = tableName;
    }

    public void setSourceRows(long count) {
        this.sourceRows = count;
    }

    public void incrementAccepted() {
        acceptedRows++;
    }

    public void incrementInvalid() {
        skippedInvalidRows++;
    }

    public void incrementMissingReference() {
        skippedMissingReferences++;
    }

    public void setDatabaseRowsBefore(long count) {
        databaseRowsBefore = count;
    }

    public void setDatabaseRowsAfter(long count) {
        databaseRowsAfter = count;
    }

    public void printReport() {
        System.out.println("===== " + tableName + " =====");
        System.out.println("Source rows: " + sourceRows);
        System.out.println("Accepted rows: " + acceptedRows);
        System.out.println("Invalid rows: " + skippedInvalidRows);
        System.out.println(
                "Missing references: " + skippedMissingReferences
        );
        System.out.println(
                "New database rows: "
                        + (databaseRowsAfter - databaseRowsBefore)
        );
        System.out.println(
                "Total database rows: " + databaseRowsAfter
        );
    }
}
