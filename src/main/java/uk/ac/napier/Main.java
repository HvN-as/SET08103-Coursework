
package uk.ac.napier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {

        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (url == null || url.isBlank()) {
            System.err.println("DB_URL environment variable is not set.");
            System.exit(1);
            return;
        }

        if (username == null || username.isBlank()) {
            System.err.println("DB_USER environment variable is not set.");
            System.exit(1);
            return;
        }

        if (password == null || password.isBlank()) {
            System.err.println("DB_PASSWORD environment variable is not set.");
            System.exit(1);
            return;
        }

        try (Connection connection =
                     DriverManager.getConnection(url, username, password)) {

            System.out.println(
                    "Successfully connected to the World database!");

            CapitalCityReports capitalReports =
                    new CapitalCityReports(connection);

            ConsoleMenu menu = new ConsoleMenu(
                    capitalReports,
                    System.in,
                    System.out
            );

            menu.run();

        } catch (SQLException e) {
            System.err.println(
                    "Database error: " + e.getMessage());
            System.exit(1);
        }
    }

    // Existing country reports preserved for later menu integration.
    // These are not executed automatically.

    private static void runCountryReports(Connection connection)
            throws SQLException {

        // REPORT 1: All countries worldwide by population
        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                ORDER BY country.Population DESC
                """;

        System.out.println(
                "\nREPORT 1: All countries in the world by population");

        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sql)) {

            printCountryHeader();

            while (results.next()) {
                printCountryRow(results);
            }
        }

        // REPORT 2: Countries in a selected continent
        String continentSql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Continent = ?
                ORDER BY country.Population DESC
                """;

        String selectedContinent = "Africa";

        System.out.println(
                "\nREPORT 2: Countries in " + selectedContinent
                        + " by population");

        try (PreparedStatement statement =
                     connection.prepareStatement(continentSql)) {

            statement.setString(1, selectedContinent);

            try (ResultSet results = statement.executeQuery()) {

                printCountryHeader();

                while (results.next()) {
                    printCountryRow(results);
                }
            }
        }

        // REPORT 3: Countries in a selected region
        String regionSql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Region = ?
                ORDER BY country.Population DESC
                """;

        String selectedRegion = "Eastern Africa";

        System.out.println(
                "\nREPORT 3: Countries in " + selectedRegion
                        + " by population");

        try (PreparedStatement statement =
                     connection.prepareStatement(regionSql)) {

            statement.setString(1, selectedRegion);

            try (ResultSet results = statement.executeQuery()) {

                printCountryHeader();

                while (results.next()) {
                    printCountryRow(results);
                }
            }
        }

        // REPORT 4: Top N countries worldwide
        String topCountriesSql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                ORDER BY country.Population DESC
                LIMIT ?
                """;

        int topN = 10;

        System.out.println(
                "\nREPORT 4: Top " + topN
                        + " countries worldwide by population");

        try (PreparedStatement statement =
                     connection.prepareStatement(topCountriesSql)) {

            statement.setInt(1, topN);

            try (ResultSet results = statement.executeQuery()) {

                printCountryHeader();

                while (results.next()) {
                    printCountryRow(results);
                }
            }
        }

        // REPORT 5: Top N countries in a continent
        String topContinentSql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Continent = ?
                ORDER BY country.Population DESC
                LIMIT ?
                """;

        String topContinent = "Africa";
        int topContinentN = 10;

        System.out.println(
                "\nREPORT 5: Top " + topContinentN
                        + " countries in " + topContinent);

        try (PreparedStatement statement =
                     connection.prepareStatement(topContinentSql)) {

            statement.setString(1, topContinent);
            statement.setInt(2, topContinentN);

            try (ResultSet results = statement.executeQuery()) {

                printCountryHeader();

                while (results.next()) {
                    printCountryRow(results);
                }
            }
        }

        // REPORT 6: Top N countries in a region
        String topRegionSql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Region = ?
                ORDER BY country.Population DESC
                LIMIT ?
                """;

        String topRegion = "Eastern Africa";
        int topRegionN = 10;

        System.out.println(
                "\nREPORT 6: Top " + topRegionN
                        + " countries in " + topRegion);

        try (PreparedStatement statement =
                     connection.prepareStatement(topRegionSql)) {

            statement.setString(1, topRegion);
            statement.setInt(2, topRegionN);

            try (ResultSet results = statement.executeQuery()) {

                printCountryHeader();

                while (results.next()) {
                    printCountryRow(results);
                }
            }
        }
    }

    // Print country report headings
    private static void printCountryHeader() {

        System.out.printf(
                "%-5s %-35s %-15s %-30s %-15s %-30s%n",
                "Code",
                "Name",
                "Continent",
                "Region",
                "Population",
                "Capital"
        );
    }

    // Print country report rows
    private static void printCountryRow(ResultSet results)
            throws SQLException {

        String capital = results.getString("Capital");

        if (capital == null) {
            capital = "N/A";
        }

        System.out.printf(
                "%-5s %-35s %-15s %-30s %-15d %-30s%n",
                results.getString("Code"),
                results.getString("Name"),
                results.getString("Continent"),
                results.getString("Region"),
                results.getLong("Population"),
                capital
        );
    }
}
