package com.pluralsight;

public class LedgerEntry {
        private String date;
        private String description;
        private String vendor;
        private double amount;
        private String type; // "deposit" or "debit"

        // Constructor
        public LedgerEntry(String date, String description, String vendor, double amount, String type) {
            this.date = date;
            this.description = description;
            this.vendor = vendor;
            this.amount = amount;
            this.type = type;
        }}
