package uk.ac.napier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/world";
        String username = "root";
        String password = System.getenv("DB_PASSWORD");

        if (password == null || password.isBlank()) {
            System.err.println("DB_PASSWORD environment variable is not set.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(url, username, password)) {

            System.out.println("Successfully connected to the World database!");

            // REPORT 1: All countries in the world by population
            String sql = """
                    SELECT country.Code, country.Name, country.Continent,
                           country.Region, country.Population, city.Name AS Capital
                    FROM country
                    LEFT JOIN city ON country.Capital = city.ID
                    ORDER BY country.Population DESC
                    """;

            System.out.println("\nREPORT 1: All countries in the world by population");

            try (Statement statement = connection.createStatement();
                 ResultSet results = statement.executeQuery(sql)) {

                printCountryHeader();

                while (results.next()) {
                    printCountryRow(results);
                }
            }

            // REPORT 2: All countries in a selected continent by population
            String continentSql = """
                    SELECT country.Code, country.Name, country.Continent,
                           country.Region, country.Population, city.Name AS Capital
                    FROM country
                    LEFT JOIN city ON country.Capital = city.ID
                    WHERE country.Continent = ?
                    ORDER BY country.Population DESC
                    """;

            String selectedContinent = "Africa";

            System.out.println("\nREPORT 2: Countries in " + selectedContinent + " by population");

            try (PreparedStatement continentStatement = connection.prepareStatement(continentSql)) {

                continentStatement.setString(1, selectedContinent);

                try (ResultSet continentResults = continentStatement.executeQuery()) {

                    printCountryHeader();

                    while (continentResults.next()) {
                        printCountryRow(continentResults);
                    }
                }
            }

            // REPORT 3: All countries in a selected region by population
            String regionSql = """
                    SELECT country.Code, country.Name, country.Continent,
                           country.Region, country.Population, city.Name AS Capital
                    FROM country
                    LEFT JOIN city ON country.Capital = city.ID
                    WHERE country.Region = ?
                    ORDER BY country.Population DESC
                    """;

            String selectedRegion = "Eastern Africa";

            System.out.println("\nREPORT 3: Countries in " + selectedRegion + " by population");

            try (PreparedStatement regionStatement = connection.prepareStatement(regionSql)) {

                regionStatement.setString(1, selectedRegion);

                try (ResultSet regionResults = regionStatement.executeQuery()) {

                    printCountryHeader();

                    while (regionResults.next()) {
                        printCountryRow(regionResults);
                    }
                }
            }

            // REPORT 4: Top N countries in the world by population
            String topCountriesSql = """
        SELECT country.Code, country.Name, country.Continent,
               country.Region, country.Population, city.Name AS Capital
        FROM country
        LEFT JOIN city ON country.Capital = city.ID
        ORDER BY country.Population DESC
        LIMIT ?
        """;

            int topN = 10;

            System.out.println("\nREPORT 4: Top " + topN + " countries in the world by population");

            try (PreparedStatement topCountriesStatement =
                         connection.prepareStatement(topCountriesSql)) {

                topCountriesStatement.setInt(1, topN);

                try (ResultSet topCountriesResults =
                             topCountriesStatement.executeQuery()) {

                    printCountryHeader();

                    while (topCountriesResults.next()) {
                        printCountryRow(topCountriesResults);
                    }
                }
            }

            // REPORT 5: Top N countries in a selected continent by population
            String topContinentSql = """
        SELECT country.Code, country.Name, country.Continent,
               country.Region, country.Population, city.Name AS Capital
        FROM country
        LEFT JOIN city ON country.Capital = city.ID
        WHERE country.Continent = ?
        ORDER BY country.Population DESC
        LIMIT ?
        """;

            String topContinent = "Africa";
            int topContinentN = 10;

            System.out.println("\nREPORT 5: Top " + topContinentN
                    + " countries in " + topContinent + " by population");

            try (PreparedStatement topContinentStatement =
                         connection.prepareStatement(topContinentSql)) {

                topContinentStatement.setString(1, topContinent);
                topContinentStatement.setInt(2, topContinentN);

                try (ResultSet topContinentResults =
                             topContinentStatement.executeQuery()) {

                    printCountryHeader();

                    while (topContinentResults.next()) {
                        printCountryRow(topContinentResults);
                    }
                }
            }
            // REPORT 6: Top N countries in a selected region by population
            String topRegionSql = """
        SELECT country.Code, country.Name, country.Continent,
               country.Region, country.Population, city.Name AS Capital
        FROM country
        LEFT JOIN city ON country.Capital = city.ID
        WHERE country.Region = ?
        ORDER BY country.Population DESC
        LIMIT ?
        """;

            String topRegion = "Eastern Africa";
            int topRegionN = 10;

            System.out.println("\nREPORT 6: Top " + topRegionN
                    + " countries in " + topRegion + " by population");

            try (PreparedStatement topRegionStatement =
                         connection.prepareStatement(topRegionSql)) {

                topRegionStatement.setString(1, topRegion);
                topRegionStatement.setInt(2, topRegionN);

                try (ResultSet topRegionResults =
                             topRegionStatement.executeQuery()) {

                    printCountryHeader();

                    while (topRegionResults.next()) {
                        printCountryRow(topRegionResults);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // Print the country report column headings
    private static void printCountryHeader() {

        System.out.printf("%-5s %-35s %-15s %-30s %-15s %-30s%n",
                "Code", "Name", "Continent", "Region", "Population", "Capital");
    }

    // Print each country's details
    private static void printCountryRow(ResultSet results) throws SQLException {

        String capital = results.getString("Capital");

        if (capital == null) {
            capital = "N/A";
        }

        System.out.printf("%-5s %-35s %-15s %-30s %-15d %-30s%n",
                results.getString("Code"),
                results.getString("Name"),
                results.getString("Continent"),
                results.getString("Region"),
                results.getLong("Population"),
                capital);
    }
}