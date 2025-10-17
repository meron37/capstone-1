package com.pluralsight;

import java.io.*; // read and write
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/main/resources/transactions.csv"; // accessible, unchangeable and the uppercase is signal to other developers it's constant don’t try to change it

    // String FILE_PATH = "src/main/resources/transactions.csv";

    static Scanner scanner; //  Global Scanner

    public static void main(String[] args) {
        scanner = new Scanner(System.in); //  Initialize Scanner

        // === LOGIN FIRST ===
        if (!Auth.login(scanner)) {
            System.out.println("Access denied. Exiting application.");
            return;
        }

        String choice; // variable declaration

        // Main application loop it's executes the selected option (Deposit, Payment, Ledger, or Exit)
        //  Repeats until the user enters "X" to exit
        do {
            printHomeScreen(); // method call to displays the main menu
            choice = scanner.nextLine().trim().toUpperCase(); // toUpperCase used to convert lower case to uppercase incase user enter

            switch (choice) {
                case "D":
                    addDeposit(); // Handle deposit input and save /since Scanner is global No scanner to pass
                    break;
                case "P":
                    makePayment();
                    break;
                case "L":
                    showLedgerMenu();
                    break;
                case "X":
                    System.out.println("Exiting application...");
                    break;
                default:
                    System.out.println("Invalid input. Please enter D, P, L, or X.");
            }

        }
        // Keep looping as long as the user’s choice is not equal to X
        while (!choice.equals("X"));

        scanner.close(); // Close once at the end
    }

    // === HOME MENU ====
    private static void printHomeScreen() {
        System.out.println("\n=== Ledger Application ===");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
        System.out.print("Enter your choice: ");
    }

    // ======== LEDGER MENU ========
    private static void showLedgerMenu() {
        String choice;

        do {
            System.out.println("\n=== Ledger Menu ===");
            System.out.println("A) All Entries");
            System.out.println("D) Deposits Only");
            System.out.println("P) Payments Only");
            System.out.println("R) Reports");
            System.out.println("H) Home");
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine().trim().toUpperCase();

            switch (choice) {
                case "A":
                    showAllEntries();  // since Scanner is global No scanner to pass
                    break;
                case "D":
                    showDeposits();
                    break;
                case "P":
                    showPayments();
                    break;
                case "R":
                    showReportsMenu();
                    break;
                case "H":
                    System.out.println("Returning to Home screen...");
                    break;
                default:
                    System.out.println("Invalid input.");
            }

        } while (!choice.equals("H"));
    }

    // ======== REPORTS MENU (Updated with real date filters) ========
    private static void showReportsMenu() {
        String choice;

        do {
            System.out.println("\n=== Reports Menu ===");
            System.out.println("1) Month to Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year to Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search"); // Bonus
            System.out.println("0) Back to Ledger Menu");
            System.out.print("Enter your choice: ");
            choice = scanner.nextLine().trim();

            List<Transactions> entries = readEntries(); // list of all transactions
            Collections.reverse(entries); // newest first
            LocalDate today = LocalDate.now();

            switch (choice) {
                case "1": // Month to Date
                    LocalDate startOfMonth = today.withDayOfMonth(1); // 1 refers to the first day of the current month
                    System.out.println("Results from " + startOfMonth + " to " + today);
                    for (Transactions entry : entries) {
                        LocalDate transactionDate = LocalDate.parse(entry.getDate());
                        if (!transactionDate.isBefore(startOfMonth) && !transactionDate.isAfter(today)) { //
                            System.out.println(entry);
                        }
                    }
                    break;

                case "2": // Previous Month
                    LocalDate firstDayPrevMonth = today.minusMonths(1).withDayOfMonth(1); // minuMonth - goes back one month
                    LocalDate lastDayPrevMonth = firstDayPrevMonth.withDayOfMonth(firstDayPrevMonth.lengthOfMonth());
                    System.out.println("Results from " + firstDayPrevMonth + " to " + lastDayPrevMonth);
                    for (Transactions entry : entries) {
                        LocalDate transactionDate = LocalDate.parse(entry.getDate());
                        if (!transactionDate.isBefore(firstDayPrevMonth) && !transactionDate.isAfter(lastDayPrevMonth)) {
                            System.out.println(entry);
                        }
                    }
                    break;

                case "3": // Year to Date
                    LocalDate startOfYear = today.withDayOfYear(1);
                    System.out.println("Results from " + startOfYear + " to " + today);
                    for (Transactions entry : entries) {
                        LocalDate transactionDate = LocalDate.parse(entry.getDate());
                        if (!transactionDate.isBefore(startOfYear) && !transactionDate.isAfter(today)) {
                            System.out.println(entry);
                        }
                    }
                    break;

                case "4": // Previous Year
                    LocalDate prevYearStart = LocalDate.of(today.getYear() - 1, 1, 1);
                    LocalDate prevYearEnd = LocalDate.of(today.getYear() - 1, 12, 31);
                    System.out.println("Results from " + prevYearStart + " to " + prevYearEnd);
                    for (Transactions entry : entries) {
                        LocalDate transactionDate = LocalDate.parse(entry.getDate());
                        if (!transactionDate.isBefore(prevYearStart) && !transactionDate.isAfter(prevYearEnd)) {
                            System.out.println(entry);
                        }
                    }
                    break;

                case "5": // Search by Vendor
                    System.out.print("Enter vendor name: ");
                    String vendor = scanner.nextLine().trim();
                    List<Transactions> matches = new ArrayList<>(); // Empty list to store search results

                    for (Transactions entry : entries) {
                        if (entry.getVendor().equalsIgnoreCase(vendor)) {
                            matches.add(entry);
                        }
                    }

                    if (matches.isEmpty()) {
                        System.out.println("No transactions found for vendor: " + vendor);
                    } else {
                        System.out.println("Results for vendor: " + vendor);
                        for (Transactions entry : matches) {
                            System.out.println(entry);
                        }
                    }
                    break;

                case "6": // Custom Search
                    System.out.println("\n--- Custom Search ---");

                    // === validate dates with do/while so we never crash on bad input ===
                    String startDateInput;
                    LocalDate startDate = null;
                    do {
                        System.out.print("Start date (yyyy-MM-dd), or leave blank: ");
                        startDateInput = scanner.nextLine().trim();
                        if (startDateInput.isEmpty()) break;
                        try {
                            startDate = LocalDate.parse(startDateInput);
                            break; // valid -> exit loop
                        } catch (Exception e) {
                            System.out.println("Invalid date. Please use yyyy-MM-dd or leave blank.");
                        }
                    } while (true);

                    String endDateInput;
                    LocalDate endDate = null;
                    do {
                        System.out.print("End date (yyyy-MM-dd), or leave blank: ");
                        endDateInput = scanner.nextLine().trim();
                        if (endDateInput.isEmpty()) break;
                        try {
                            endDate = LocalDate.parse(endDateInput);
                            break;
                        } catch (Exception e) {
                            System.out.println("Invalid date. Please use yyyy-MM-dd or leave blank.");
                        }
                    } while (true);

                    System.out.print("Description, or leave blank: ");
                    String descriptionInput = scanner.nextLine().trim();

                    System.out.print("Vendor, or leave blank: ");
                    String vendorInput = scanner.nextLine().trim();

                    // Amount validation with do/while (blank allowed)
                    String amountInput;
                    Double amountFilter = null;
                    do {
                        System.out.print("Amount (exact match), or leave blank: ");
                        amountInput = scanner.nextLine().trim();
                        if (amountInput.isEmpty()) break;
                        try {
                            amountFilter = Double.parseDouble(amountInput);
                            break;
                        } catch (NumberFormatException nfe) {
                            System.out.println("Invalid amount. Enter a number or leave blank.");
                        }
                    } while (true);

                    boolean found = false;
                    for (Transactions entry : entries) {
                        boolean matchesCustom = true;

                        if (startDate != null) {
                            LocalDate txDate = LocalDate.parse(entry.getDate());
                            if (txDate.isBefore(startDate)) matchesCustom = false;
                        }

                        if (matchesCustom && endDate != null) {
                            LocalDate txDate = LocalDate.parse(entry.getDate());
                            if (txDate.isAfter(endDate)) matchesCustom = false;
                        }

                        if (matchesCustom && !descriptionInput.isEmpty()
                                && !entry.getDescription().equalsIgnoreCase(descriptionInput)) {
                            matchesCustom = false;
                        }

                        if (matchesCustom && !vendorInput.isEmpty()
                                && !entry.getVendor().equalsIgnoreCase(vendorInput)) {
                            matchesCustom = false;
                        }

                        if (matchesCustom && amountFilter != null) {
                            if (Double.compare(entry.getAmount(), amountFilter) != 0) matchesCustom = false;
                        }

                        if (matchesCustom) {
                            System.out.println(entry);
                            found = true;
                        }
                    }

                    if (!found) {
                        System.out.println("No results found for your search.");
                    }
                    break;

                case "0":
                    System.out.println("Returning to Ledger Menu...");
                    break;

                default:
                    System.out.println("Invalid input. Please select from 1–6 or 0.");
            }

        } while (!choice.equals("0"));
    }

    // Handles deposit input from the user and saves it to the CSV file
    private static void addDeposit() {
        System.out.println("\n--- Add Deposit ---");
        Transactions entry = promptTransaction(false); // false = not a payment
        writeEntry(entry);
        System.out.println("Deposit saved successfully!");
    }

    private static void makePayment() {
        System.out.println("\n--- Make Payment ---");
        Transactions entry = promptTransaction(true); // true = payment
        writeEntry(entry);
        System.out.println("Payment saved successfully!");
    }

    private static Transactions promptTransaction(boolean isPayment) {

        // Prompt user for transaction details
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter vendor: ");
        String vendor = scanner.nextLine();

        double amount = 0;
        boolean gotNumber = false;
        do {
            try {
                System.out.print("Enter amount: ");
                amount = Double.parseDouble(scanner.nextLine());
                gotNumber = true;
            } catch (NumberFormatException e) {
                System.out.println("Input Error: Invalid number for amount.");
            }
        } while (!gotNumber);

        if (isPayment) amount = -Math.abs(amount); // Make sure payments are negative

        // Get today's date and current time
        String date = LocalDate.now().toString();
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss a")); // a is for AM/PM

        return new Transactions(date, time, description, vendor, amount);
    }

    // Saves a transaction entry to the transactions.csv file.
    private static void writeEntry(Transactions entry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(String.join("|", // Combines multiple pieces of text into one line, separated by "|"
                    entry.getDate(),
                    entry.getTime(),
                    entry.getDescription(),
                    entry.getVendor(),
                    String.valueOf(entry.getAmount()) // Converts any number (int, double) into a String
            ));
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    /* reading from the file, creating a Transactions object (entry), and then adding each entry to an ArrayList
       so we can later display or filter
    */
    private static List<Transactions> readEntries() {
        List<Transactions> entries = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return entries;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|"); // \\ escape character - because java needs "\" to be writeen as \\
                if (parts.length == 5) {
                    Transactions entry = new Transactions(
                            parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4])
                    );
                    entries.add(entry); // Adds transaction object to the list
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return entries;
    }

    // Displays all transactions from the CSV file in reverse // like stack last in fist out
    private static void showAllEntries() {
        List<Transactions> entries = readEntries(); // calling redEntries method all transactions are stored in Arraylist "entries"
        entries.sort(Comparator.comparing(Transactions::getDate).reversed()); // replaced Collections.sort with List.sort :: is Method reference
        for (Transactions entry : entries) {
            System.out.println(entry);
        }
    }

    // Displays only deposit (positive amount) transactions from the file
    private static void showDeposits() {
        List<Transactions> entries = readEntries();
        List<Transactions> deposits = new ArrayList<>();

        for (Transactions entry : entries) { // : mean "in"  // For each entry in entries
            if (entry.getAmount() > 0) {
                deposits.add(entry);
            }
        }

        deposits.sort(Comparator.comparingDouble(Transactions::getAmount)); // sort by SMALLEST deposit first (ascending)

        for (Transactions deposit : deposits) {
            System.out.println(deposit);
        }
    }

     // Displays only payment (negative amount) transactions from the CSV file
    private static void showPayments() {
        List<Transactions> entries = readEntries();
        List<Transactions> payments = new ArrayList<>();

        for (Transactions entry : entries) {
            if (entry.getAmount() < 0) {
                payments.add(entry);
            }
        }

        payments.sort(Comparator.comparingDouble(Transactions::getAmount)); // most negative first

        for (Transactions payment : payments) {
            System.out.println(payment);
        }
    }
}
