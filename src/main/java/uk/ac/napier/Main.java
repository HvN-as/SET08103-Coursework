
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

        try (Connection connection =
                     DriverManager.getConnection(url, username, password)) {

            System.out.println("Successfully connected to the World database!");

            // REPORT 1: All countries in the world by population
            String sql = """
                    SELECT country.Code,
                           country.Name,
                           country.Continent,
                           country.Region,
                           country.Population,
                           city.Name AS Capital
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
                    SELECT country.Code,
                           country.Name,
                           country.Continent,
                           country.Region,
                           country.Population,
                           city.Name AS Capital
                    FROM country
                    LEFT JOIN city ON country.Capital = city.ID
                    WHERE country.Continent = ?
                    ORDER BY country.Population DESC
                    """;

            String selectedContinent = "Africa";

            System.out.println(
                    "\nREPORT 2: Countries in " + selectedContinent + " by population"
            );

            try (PreparedStatement continentStatement =
                         connection.prepareStatement(continentSql)) {

                continentStatement.setString(1, selectedContinent);

                try (ResultSet continentResults =
                             continentStatement.executeQuery()) {

                    printCountryHeader();

                    while (continentResults.next()) {
                        printCountryRow(continentResults);
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // Prints the six required column headings
    private static void printCountryHeader() {
        System.out.printf(
                "%-5s %-35s %-15s %-30s %-15s %-30s%n",
                "Code", "Name", "Continent", "Region", "Population", "Capital"
        );
    }

    // Prints one country with all six required columns
    private static void printCountryRow(ResultSet results) throws SQLException {

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
