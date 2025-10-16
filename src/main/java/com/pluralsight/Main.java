package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/main/resources/transactions.csv"; // accessible, unchangeable and the uppercase is signal to other developers it's constant don’t try to change it

    static Scanner scanner; //  Global Scanner

    public static void main(String[] args) {
        scanner = new Scanner(System.in); //  Initialize Scanner
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

    // ======== HOME MENU ========
    private static void printHomeScreen() {
        System.out.println("\n=== Ledger Application ===");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment (Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
        System.out.print("Enter your choice: ");
    }

}
