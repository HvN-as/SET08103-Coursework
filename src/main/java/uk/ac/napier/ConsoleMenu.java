
package uk.ac.napier;

import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ConsoleMenu {

    // Store the report classes, database connection and input/output
    private final CapitalCityReports capitalReports;
    private final Connection connection;
    private final Scanner scanner;
    private final PrintStream output;

    // Constructor used by the existing capital city report tests
    public ConsoleMenu(CapitalCityReports capitalReports,
                       InputStream input,
                       PrintStream output) {
        this(capitalReports, null, input, output);
    }

    // Constructor used by the main application
    public ConsoleMenu(CapitalCityReports capitalReports,
                       Connection connection,
                       InputStream input,
                       PrintStream output) {

        this.capitalReports = capitalReports;
        this.connection = connection;
        this.scanner = new Scanner(input);
        this.output = output;
    }

    // Display the menu and allow users to select reports
    public void run() throws SQLException {

        // Keep displaying the menu until the user chooses to exit
        while (true) {

            output.println("\n=== World Population Reports ===");

            // Capital city report options
            output.println("\n--- Capital City Reports ---");
            output.println("1. All capital cities worldwide");
            output.println("2. Capital cities by continent");
            output.println("3. Capital cities by region");
            output.println("4. Top capital cities worldwide");
            output.println("5. Top capital cities by continent");
            output.println("6. Top capital cities by region");

            // City population report options
            output.println("\n--- City Population Reports ---");
            output.println("7. All cities worldwide");
            output.println("8. Cities by continent");
            output.println("9. Cities by region");
            output.println("10. Cities by country");
            output.println("11. Cities by district");
            output.println("12. Top cities worldwide");
            output.println("13. Top cities by continent");
            output.println("14. Top cities by region");
            output.println("15. Top cities by country");
            output.println("16. Top cities by district");

            // Country population report options
            output.println("\n--- Country Population Reports ---");
            output.println("17. All countries worldwide");
            output.println("18. Countries by continent");
            output.println("19. Countries by region");
            output.println("20. Top countries worldwide");
            output.println("21. Top countries by continent");
            output.println("22. Top countries by region");

            // Population total report options
            output.println("\n--- Population Total Reports ---");
            output.println("23. World population");
            output.println("24. Population of a continent");
            output.println("25. Population of a region");
            output.println("26. Population of a country");
            output.println("27. Population of a district");
            output.println("28. Population of a city");

            // Language population report option
            output.println("\n--- Language Population Reports ---");
            output.println("29. Major language population report");

            // Exit option
            output.println("\n0. Exit");
            output.print("Select an option: ");

            // Check whether input is available
            if (!scanner.hasNextLine()) {
                output.println("\nInput closed. Exiting.");
                return;
            }

            // Read the user's menu selection
            String choice = scanner.nextLine().trim();

            // Exit when the user enters 0
            if (choice.equals("0")) {
                output.println("Goodbye!");
                return;
            }

            try {

                // Run the report selected by the user
                switch (choice) {

                    // CAPITAL CITY REPORTS

                    // Report 1: All capital cities worldwide
                    case "1" -> capitalReports.allCapitalsWorldwide();

                    // Report 2: Capital cities in a continent
                    case "2" -> capitalReports.capitalsByContinent(
                            readRequiredText("Enter continent: "));

                    // Report 3: Capital cities in a region
                    case "3" -> capitalReports.capitalsByRegion(
                            readRequiredText("Enter region: "));

                    // Report 4: Top N capital cities worldwide
                    case "4" -> capitalReports.topCapitalsWorldwide(
                            readPositiveNumber("capitals"));

                    // Report 5: Top N capitals in a continent
                    case "5" -> capitalReports.topCapitalsByContinent(
                            readRequiredText("Enter continent: "),
                            readPositiveNumber("capitals"));

                    // Report 6: Top N capitals in a region
                    case "6" -> capitalReports.topCapitalsByRegion(
                            readRequiredText("Enter region: "),
                            readPositiveNumber("capitals"));


                    // CITY POPULATION REPORTS

                    // Report 7: All cities worldwide
                    case "7" -> {
                        requireCityConnection();
                        CityReport.showAllCities(connection);
                    }

                    // Report 8: Cities in a continent
                    case "8" -> {
                        requireCityConnection();
                        CityReport.showCitiesByContinent(
                                connection,
                                readRequiredText("Enter continent: "));
                    }

                    // Report 9: Cities in a region
                    case "9" -> {
                        requireCityConnection();
                        CityReport.showCitiesByRegion(
                                connection,
                                readRequiredText("Enter region: "));
                    }

                    // Report 10: Cities in a country
                    case "10" -> {
                        requireCityConnection();
                        CityReport.showCitiesByCountry(
                                connection,
                                readRequiredText("Enter country: "));
                    }

                    // Report 11: Cities in a district
                    case "11" -> {
                        requireCityConnection();
                        CityReport.showCitiesByDistrict(
                                connection,
                                readRequiredText("Enter district: "));
                    }

                    // Report 12: Top N cities worldwide
                    case "12" -> {
                        requireCityConnection();
                        CityReport.showTopNCitiesWorldwide(
                                connection,
                                readPositiveNumber("cities"));
                    }

                    // Report 13: Top N cities in a continent
                    case "13" -> {
                        requireCityConnection();
                        CityReport.showTopNCitiesByContinent(
                                connection,
                                readRequiredText("Enter continent: "),
                                readPositiveNumber("cities"));
                    }

                    // Report 14: Top N cities in a region
                    case "14" -> {
                        requireCityConnection();
                        CityReport.showTopNCitiesByRegion(
                                connection,
                                readRequiredText("Enter region: "),
                                readPositiveNumber("cities"));
                    }

                    // Report 15: Top N cities in a country
                    case "15" -> {
                        requireCityConnection();
                        CityReport.showTopNCitiesByCountry(
                                connection,
                                readRequiredText("Enter country: "),
                                readPositiveNumber("cities"));
                    }

                    // Report 16: Top N cities in a district
                    case "16" -> {
                        requireCityConnection();
                        CityReport.showTopNCitiesByDistrict(
                                connection,
                                readRequiredText("Enter district: "),
                                readPositiveNumber("cities"));
                    }


                    // COUNTRY POPULATION REPORTS

                    // Report 17: All countries worldwide
                    case "17" -> CountryReport.displayCountries();

                    // Report 18: Countries in a selected continent
                    case "18" -> CountryReport.displayCountriesByContinent(
                            readRequiredText("Enter continent: "));

                    // Report 19: Countries in a selected region
                    case "19" -> CountryReport.displayCountriesByRegion(
                            readRequiredText("Enter region: "));

                    // Report 20: Top N countries worldwide
                    case "20" -> CountryReport.displayTopCountries(
                            readPositiveNumber("countries"));

                    // Report 21: Top N countries in a continent
                    case "21" -> CountryReport.displayTopCountriesByContinent(
                            readRequiredText("Enter continent: "),
                            readPositiveNumber("countries"));

                    // Report 22: Top N countries in a region
                    case "22" -> CountryReport.displayTopCountriesByRegion(
                            readRequiredText("Enter region: "),
                            readPositiveNumber("countries"));


                    // POPULATION TOTAL REPORTS

                    // Report 23: World population
                    case "23" -> {
                        requireCityConnection();
                        PopulationTotalReports reports =
                                new PopulationTotalReports(connection);
                        output.println("World population: " +
                                reports.worldPopulation());
                    }

                    // Report 24: Population of a continent
                    case "24" -> {
                        requireCityConnection();
                        PopulationTotalReports reports =
                                new PopulationTotalReports(connection);
                        String continent =
                                readRequiredText("Enter continent: ");
                        output.println("Population of " + continent + ": " +
                                reports.continentPopulation(continent));
                    }

                    // Report 25: Population of a region
                    case "25" -> {
                        requireCityConnection();
                        PopulationTotalReports reports =
                                new PopulationTotalReports(connection);
                        String region =
                                readRequiredText("Enter region: ");
                        output.println("Population of " + region + ": " +
                                reports.regionPopulation(region));
                    }

                    // Report 26: Population of a country
                    case "26" -> {
                        requireCityConnection();
                        PopulationTotalReports reports =
                                new PopulationTotalReports(connection);
                        String country =
                                readRequiredText("Enter country: ");
                        output.println("Population of " + country + ": " +
                                reports.countryPopulation(country));
                    }

                    // Report 27: Population of a district
                    case "27" -> {
                        requireCityConnection();
                        PopulationTotalReports reports =
                                new PopulationTotalReports(connection);
                        String district =
                                readRequiredText("Enter district: ");
                        output.println("Population of " + district + ": " +
                                reports.districtPopulation(district));
                    }

                    // Report 28: Population of a city
                    case "28" -> {
                        requireCityConnection();
                        PopulationTotalReports reports =
                                new PopulationTotalReports(connection);
                        String city =
                                readRequiredText("Enter city: ");
                        output.println("Population of " + city + ": " +
                                reports.cityPopulation(city));
                    }


                    // LANGUAGE POPULATION REPORT

                    // Report 29: Major language population report
                    case "29" -> {
                        requireCityConnection();
                        LanguagePopulationReports reports =
                                new LanguagePopulationReports(connection);
                        reports.showLanguageReport(output);
                    }

                    // Handle an option that does not exist
                    default -> output.println(
                            "Invalid menu option. Please try again.");
                }

            } catch (IllegalArgumentException |
                     IllegalStateException e) {

                // Show an error without closing the menu
                output.println("Invalid input: " + e.getMessage());
            }
        }
    }

    // Check that a database connection is available for reports
    private void requireCityConnection() {

        if (connection == null) {
            throw new IllegalStateException(
                    "City reports are not configured.");
        }
    }

    // Read text such as a continent, region or country
    private String readRequiredText(String prompt) {

        // Ask the user to enter a value
        output.print(prompt);

        // Check that input is available
        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException(
                    "Input is required.");
        }

        // Remove extra spaces before and after the input
        String value = scanner.nextLine().trim();

        // Reject blank input
        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    "Value cannot be blank.");
        }

        // Return the text entered
        return value;
    }

    // Read a positive number for a Top N report
    private int readPositiveNumber(String reportType) {

        // Ask for the number of countries, cities or capitals
        output.print("Enter number of " + reportType + ": ");

        // Check that input is available
        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException(
                    "A positive number is required.");
        }

        String value = scanner.nextLine().trim();

        try {

            // Convert the text into a whole number
            int number = Integer.parseInt(value);

            // Reject zero or negative numbers
            if (number <= 0) {
                throw new IllegalArgumentException(
                        "Number must be greater than zero.");
            }

            // Return the valid number
            return number;

        } catch (NumberFormatException e) {

            // Handle letters or other invalid input
            throw new IllegalArgumentException(
                    "Please enter a valid whole number.");
        }
    }
}
