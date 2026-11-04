package uk.ac.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryReport {

    public static void displayCountries() {
        String sql = """
                SELECT Name, Continent, Region, Population
                FROM country
                ORDER BY Population DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            System.out.printf("%-40s %-20s %-30s %15s%n",
                    "Country", "Continent", "Region", "Population");

            System.out.println("-".repeat(110));

            while (results.next()) {
                System.out.printf("%-40s %-20s %-30s %,15d%n",
                        results.getString("Name"),
                        results.getString("Continent"),
                        results.getString("Region"),
                        results.getLong("Population"));
            }

        } catch (SQLException e) {
            System.err.println("Could not generate country report: "
                    + e.getMessage());
        }
    }
}