
package uk.ac.napier;

import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleMenu {

    private final CapitalCityReports capitalReports;
    private final Connection connection;
    private final Scanner scanner;
    private final PrintStream output;

    // Preserve the existing constructor for capital-report tests.
    public ConsoleMenu(CapitalCityReports capitalReports,
                       InputStream input,
                       PrintStream output) {
        this(capitalReports, null, input, output);
    }

    // Constructor supporting both capital and city reports.
    public ConsoleMenu(CapitalCityReports capitalReports,
                       Connection connection,
                       InputStream input,
                       PrintStream output) {
        this.capitalReports = capitalReports;
        this.connection = connection;
        this.scanner = new Scanner(input);
        this.output = output;
    }

    public void run() throws SQLException {
        while (true) {
            output.println("\n=== World Population Reports ===");

            output.println("\n--- Capital City Reports ---");
            output.println("1. All capital cities worldwide");
            output.println("2. Capital cities by continent");
            output.println("3. Capital cities by region");
            output.println("4. Top N capital cities worldwide");
            output.println("5. Top N capital cities by continent");
            output.println("6. Top N capital cities by region");

            output.println("\n--- City Population Reports ---");
            output.println("7. All cities worldwide");
            output.println("8. Cities by continent");
            output.println("9. Cities by region");
            output.println("10. Cities by country");
            output.println("11. Cities by district");
            output.println("12. Top N cities worldwide");
            output.println("13. Top N cities by continent");
            output.println("14. Top N cities by region");
            output.println("15. Top N cities by country");
            output.println("16. Top N cities by district");

            output.println("\n0. Exit");
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

                    // Capital reports: existing functionality
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

                    // City population reports
                    case "7" -> {
                        requireCityConnection();
                        CityReport.showAllCities(connection);
                    }

                    case "8" -> {
                        requireCityConnection();
                        CityReport.showCitiesByContinent(
                                connection,
                                readRequiredText("Enter continent: "));
                    }

                    case "9" -> {
                        requireCityConnection();
                        CityReport.showCitiesByRegion(
                                connection,
                                readRequiredText("Enter region: "));
                    }

                    case "10" -> {
                        requireCityConnection();
                        CityReport.showCitiesByCountry(
                                connection,
                                readRequiredText("Enter country: "));
                    }

                    case "11" -> {
                        requireCityConnection();
                        CityReport.showCitiesByDistrict(
                                connection,
                                readRequiredText("Enter district: "));
                    }

                    case "12" -> {
                        requireCityConnection();
                        CityReport.showTopNCitiesWorldwide(
                                connection,
                                readPositiveNumber());
                    }

                    case "13" -> {
                        requireCityConnection();
                        CityReport.showTopNCitiesByContinent(
                                connection,
                                readRequiredText("Enter continent: "),
                                readPositiveNumber());
                    }

                    case "14" -> {
                        requireCityConnection();
                        CityReport.showTopNCitiesByRegion(
                                connection,
                                readRequiredText("Enter region: "),
                                readPositiveNumber());
                    }

                    case "15" -> {
                        requireCityConnection();
                        CityReport.showTopNCitiesByCountry(
                                connection,
                                readRequiredText("Enter country: "),
                                readPositiveNumber());
                    }

                    case "16" -> {
                        requireCityConnection();
                        CityReport.showTopNCitiesByDistrict(
                                connection,
                                readRequiredText("Enter district: "),
                                readPositiveNumber());
                    }

                    default -> output.println(
                            "Invalid menu option. Please try again.");
                }

            } catch (IllegalArgumentException |
                     IllegalStateException e) {
                output.println("Invalid input: " + e.getMessage());
            }
        }
    }

    private void requireCityConnection() {
        if (connection == null) {
            throw new IllegalStateException(
                    "City reports are not configured.");
        }
    }

    private String readRequiredText(String prompt) {
        output.print(prompt);

        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException(
                    "Input is required.");
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
