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
    }

    // Getters
    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setType(String type) {
        this.type = type;
    }
}
