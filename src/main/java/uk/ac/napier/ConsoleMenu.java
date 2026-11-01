
package uk.ac.napier;

import java.io.InputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleMenu {

    private final CapitalCityReports capitalReports;
    private final Scanner scanner;
    private final PrintStream output;

    public ConsoleMenu(CapitalCityReports capitalReports,
                       InputStream input,
                       PrintStream output) {
        this.capitalReports = capitalReports;
        this.scanner = new Scanner(input);
        this.output = output;
    }

    public void run() throws SQLException {
        while (true) {
            output.println("\n=== World Population Reports ===");
            output.println("1. All capital cities worldwide");
            output.println("2. Capital cities by continent");
            output.println("3. Capital cities by region");
            output.println("4. Top N capital cities worldwide");
            output.println("5. Top N capital cities by continent");
            output.println("6. Top N capital cities by region");
            output.println("0. Exit");
            output.print("Select an option: ");

            if (!scanner.hasNextLine()) {
                output.println("\nInput closed. Exiting.");
                return;
            }

            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) {
                output.println("Goodbye!");
                return;
            }

            try {
                switch (choice) {
                    case "1" -> capitalReports.allCapitalsWorldwide();

                    case "2" -> capitalReports.capitalsByContinent(
                            readRequiredText("Enter continent: "));

                    case "3" -> capitalReports.capitalsByRegion(
                            readRequiredText("Enter region: "));

                    case "4" -> capitalReports.topCapitalsWorldwide(
                            readPositiveNumber());

                    case "5" -> capitalReports.topCapitalsByContinent(
                            readRequiredText("Enter continent: "),
                            readPositiveNumber());

                    case "6" -> capitalReports.topCapitalsByRegion(
                            readRequiredText("Enter region: "),
                            readPositiveNumber());

                    default -> output.println(
                            "Invalid menu option. Please try again.");
                }
            } catch (IllegalArgumentException e) {
                output.println("Invalid input: " + e.getMessage());
            }
        }
    }

    private String readRequiredText(String prompt) {
        output.print(prompt);

        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException("Input is required.");
        }

        String value = scanner.nextLine().trim();

        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    "Value cannot be blank.");
        }

        return value;
    }

    private int readPositiveNumber() {
        output.print("Enter Top N: ");

        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException(
                    "A positive number is required.");
        }

        String value = scanner.nextLine().trim();

        try {
            int number = Integer.parseInt(value);

            if (number <= 0) {
                throw new IllegalArgumentException(
                        "Top N must be greater than zero.");
            }

            return number;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Please enter a valid whole number.");
        }
    }
}
