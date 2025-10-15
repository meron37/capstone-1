package com.pluralsight;

public class LedgerEntry {
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    // Constructor
    public LedgerEntry(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Getters
    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
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

    // Setters
    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
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

    // toString() for display
    @Override
    public String toString() {
        return String.format("%s | %s | %-20s | %-15s | %10.2f",
                date, time, description, vendor, amount);
    }
}
