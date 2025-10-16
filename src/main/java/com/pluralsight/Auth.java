package com.pluralsight;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Handles user login and authentication for the Ledger Application.
 * Allows up to 3 login attempts. Exits the program if authentication fails.
 */
public class Auth {

    // Store usernames (or IDs) and their matching passwords
    private static final Map<String, String> credentials = new HashMap<>();

    // Static block to initialize valid credentials
    static {
        credentials.put("admin", "1234");
        credentials.put("meron", "pass123");
        credentials.put("user01", "welcome");
        credentials.put("ID2025", "java");
    }

    /**
     * Prompts the user to enter username/ID and password.
     * Allows up to 3 attempts before access is denied.
     */
    public static boolean login(Scanner scanner) {
        final int MAX_ATTEMPTS = 3;
        int attempts = 0;

        System.out.println("=== Login Required ===");

        // Loop until correct credentials or max attempts reached
        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Enter username or ID: ");
            String username = scanner.nextLine().trim();

            System.out.print("Enter password: ");
            String password = scanner.nextLine().trim();

            // Check if username exists and password matches
            if (credentials.containsKey(username) && credentials.get(username).equals(password)) {
                System.out.println("Login successful! Welcome, " + username + ".");
                return true; // Successful login
            } else {
                attempts++;
                int remaining = MAX_ATTEMPTS - attempts;
                System.out.println("Invalid username or password.");
                if (remaining > 0) {
                    System.out.println("You have " + remaining + " attempt(s) left.\n");
                }
            }
        }

        System.out.println("Access denied. Exiting application.");
        return false; // Login failed after 3 attempts
    }
}