
package uk.ac.napier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

            try (java.sql.Statement statement = connection.createStatement();
                 java.sql.ResultSet results = statement.executeQuery(sql)) {

                System.out.printf("%-5s %-35s %-15s %-30s %-15s %-30s%n",
                        "Code", "Name", "Continent", "Region", "Population", "Capital");

                while (results.next()) {
                    System.out.printf("%-5s %-35s %-15s %-30s %-15d %-30s%n",
                            results.getString("Code"),
                            results.getString("Name"),
                            results.getString("Continent"),
                            results.getString("Region"),
                            results.getLong("Population"),
                            results.getString("Capital") == null
                                    ? "N/A" : results.getString("Capital"));
                }
            }


        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
}
