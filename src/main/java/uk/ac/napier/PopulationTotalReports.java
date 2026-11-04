
package uk.ac.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class PopulationTotalReports {

    private final Connection connection;

    public PopulationTotalReports(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    public long worldPopulation() throws SQLException {
        return queryTotal(
                "SELECT COALESCE(SUM(Population), 0) " +
                        "FROM country");
    }

    public long continentPopulation(String continent)
            throws SQLException {
        return queryTotal(
                "SELECT COALESCE(SUM(Population), 0) " +
                        "FROM country WHERE Continent = ?",
                required(continent));
    }

    public long regionPopulation(String region)
            throws SQLException {
        return queryTotal(
                "SELECT COALESCE(SUM(Population), 0) " +
                        "FROM country WHERE Region = ?",
                required(region));
    }

    public long countryPopulation(String country)
            throws SQLException {
        return queryTotal(
                "SELECT COALESCE(SUM(Population), 0) " +
                        "FROM country WHERE Name = ? OR Code = ?",
                required(country), required(country));
    }

    public long districtPopulation(String district)
            throws SQLException {
        return queryTotal(
                "SELECT COALESCE(SUM(Population), 0) " +
                        "FROM city WHERE District = ?",
                required(district));
    }

    public long cityPopulation(String city)
            throws SQLException {
        return queryTotal(
                "SELECT COALESCE(SUM(Population), 0) " +
                        "FROM city WHERE Name = ?",
                required(city));
    }

    private static String required(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "Location cannot be blank.");
        }
        return value.trim();
    }

    private long queryTotal(String sql, String... parameters)
            throws SQLException {

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.length; i++) {
                statement.setString(i + 1, parameters[i]);
            }

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return result.getLong(1);
                }
                return 0;
            }
        }
    }
}
