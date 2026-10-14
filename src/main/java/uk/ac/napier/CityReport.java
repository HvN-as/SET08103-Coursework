package uk.ac.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CityReport {

    public static void showAllCities(Connection connection)
            throws SQLException {

        String sql = """
                SELECT city.Name,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                ORDER BY city.Population DESC
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            System.out.printf("%-35s %-35s %-30s %-15s%n",
                    "Name", "Country", "District", "Population");

            while (results.next()) {
                System.out.printf("%-35s %-35s %-30s %-15d%n",
                        results.getString("Name"),
                        results.getString("Country"),
                        results.getString("District"),
                        results.getLong("Population"));
            }
        }
    }

    public static void showCitiesByContinent(
        Connection connection, String continent)
        throws SQLException {

    String sql = """
            SELECT city.Name,
                   country.Name AS Country,
                   city.District,
                   city.Population
            FROM city
            JOIN country ON city.CountryCode = country.Code
            WHERE country.Continent = ?
            ORDER BY city.Population DESC
            """;

    try (PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setString(1, continent);

        try (ResultSet results = statement.executeQuery()) {

            System.out.printf("%-35s %-35s %-30s %-15s%n",
                    "Name", "Country", "District", "Population");

            while (results.next()) {
                System.out.printf("%-35s %-35s %-30s %-15d%n",
                        results.getString("Name"),
                        results.getString("Country"),
                        results.getString("District"),
                        results.getLong("Population"));
            }
        }
    }
}
    public static void showCitiesByRegion(
        Connection connection, String region)
        throws SQLException {

    String sql = """
            SELECT city.Name,
                   country.Name AS Country,
                   city.District,
                   city.Population
            FROM city
            JOIN country ON city.CountryCode = country.Code
            WHERE country.Region = ?
            ORDER BY city.Population DESC
            """;

    try (PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setString(1, region);

        try (ResultSet results = statement.executeQuery()) {

            System.out.printf("%-35s %-35s %-30s %-15s%n",
                    "Name", "Country", "District", "Population");

            while (results.next()) {
                System.out.printf("%-35s %-35s %-30s %-15d%n",
                        results.getString("Name"),
                        results.getString("Country"),
                        results.getString("District"),
                        results.getLong("Population"));
            }
        }
    }
}

    public static void showCitiesByCountry(
        Connection connection, String country)
        throws SQLException {

    String sql = """
            SELECT city.Name,
                   country.Name AS Country,
                   city.District,
                   city.Population
            FROM city
            JOIN country ON city.CountryCode = country.Code
            WHERE country.Name = ?
            ORDER BY city.Population DESC
            """;

    try (PreparedStatement statement =
                 connection.prepareStatement(sql)) {

        statement.setString(1, country);

        try (ResultSet results = statement.executeQuery()) {

            System.out.printf("%-35s %-35s %-30s %-15s%n",
                    "Name", "Country", "District", "Population");

            while (results.next()) {
                System.out.printf("%-35s %-35s %-30s %-15d%n",
                        results.getString("Name"),
                        results.getString("Country"),
                        results.getString("District"),
                        results.getLong("Population"));
            }
        }
    }
}
    
}
