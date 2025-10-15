package com.pluralsight;

import java.io.*;
import java.util.*;

public class CsvHandler {
    private static final String FILE_PATH = "src/main/resources/transactions.csv";

    public static void writeEntry(LedgerEntry entry) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(String.join("|",
                    entry.getDate(),
                    entry.getTime(),
                    entry.getDescription(),
                    entry.getVendor(),
                    String.valueOf(entry.getAmount())
            ));
            writer.newLine();
        }
    }

    public static List<LedgerEntry> readEntries() throws IOException {
        List<LedgerEntry> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    String date = parts[0];
                    String time = parts[1];
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);

                    LedgerEntry entry = new LedgerEntry(date, time, description, vendor, amount);
                    entries.add(entry);
                }
            }
        }
        return entries;
    }
}
