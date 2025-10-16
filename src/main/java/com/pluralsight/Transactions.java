package com.pluralsight;

public class Transactions {
    // Instance variables to store transaction details
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    // Constructor creates a transaction with the given details
    public Transactions(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Getters allow us to access the private fields
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

    // Display transaction format
    @Override
    public String toString() {
        return String.format("%s | %s | %-20s | %-15s | %10.2f",
                date, time, description, vendor, amount);
    }
}