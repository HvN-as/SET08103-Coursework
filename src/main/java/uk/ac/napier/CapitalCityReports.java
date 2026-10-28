
package uk.ac.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CapitalCityReports {

    private final Connection connection;

    public CapitalCityReports(Connection connection) {
        this.connection = connection;
    }

    // Report 1: All capital cities worldwide
    public void allCapitalsWorldwide() throws SQLException {

        String sql = """
                SELECT city.Name AS Capital,
                       country.Name AS Country,
                       city.Population AS Population
                FROM country
                JOIN city ON country.Capital = city.ID
                ORDER BY city.Population DESC
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            printHeader();

            while (results.next()) {
                printRow(results);
            }
        }
    }

    // Report 2: All capital cities in a selected continent
    public void capitalsByContinent(String continent)
            throws SQLException {

        if (continent == null || continent.isBlank()) {
            throw new IllegalArgumentException(
                    "Continent cannot be blank.");
        }

        String sql = """
                SELECT city.Name AS Capital,
                       country.Name AS Country,
                       city.Population AS Population
                FROM country
                JOIN city ON country.Capital = city.ID
                WHERE country.Continent = ?
                ORDER BY city.Population DESC
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, continent);

            try (ResultSet results = statement.executeQuery()) {

                System.out.println(
                        "\nCapital cities in " + continent);

                printHeader();

                while (results.next()) {
                    printRow(results);
                }
            }
        }
    }

    private void printHeader() {
        System.out.printf("%-35s %-35s %-15s%n",
                "Capital", "Country", "Population");
    }

    private void printRow(ResultSet results) throws SQLException {
        System.out.printf("%-35s %-35s %-15d%n",
                results.getString("Capital"),
                results.getString("Country"),
                results.getLong("Population"));
    }
}
