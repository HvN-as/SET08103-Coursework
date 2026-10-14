
package uk.ac.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CityReport {

    // Displays the required city report columns and rows.
    private static void printResults(ResultSet results) throws SQLException {
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

    // Runs a city report with optional geographical filtering and Top N.
    private static void runReport(
            Connection connection,
            String filterColumn,
            String filterValue,
            Integer limit) throws SQLException {

        if (limit != null && limit <= 0) {
            throw new IllegalArgumentException(
                    "N must be greater than zero.");
        }

        String sql = """
                SELECT city.Name,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                """;

        if (filterColumn != null) {
            sql += " WHERE " + filterColumn + " = ?";
        }

        sql += " ORDER BY city.Population DESC";

        if (limit != null) {
            sql += " LIMIT ?";
        }

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            int parameterIndex = 1;

            if (filterColumn != null) {
                statement.setString(parameterIndex++, filterValue);
            }

            if (limit != null) {
                statement.setInt(parameterIndex, limit);
            }

            try (ResultSet results = statement.executeQuery()) {
                printResults(results);
            }
        }
    }

    // 1. All cities in the world.
    public static void showAllCities(Connection connection)
            throws SQLException {
        runReport(connection, null, null, null);
    }

    // 2. All cities in a continent.
    public static void showCitiesByContinent(
            Connection connection, String continent)
            throws SQLException {
        runReport(connection, "country.Continent", continent, null);
    }

    // 3. All cities in a region.
    public static void showCitiesByRegion(
            Connection connection, String region)
            throws SQLException {
        runReport(connection, "country.Region", region, null);
    }

    // 4. All cities in a country.
    public static void showCitiesByCountry(
            Connection connection, String country)
            throws SQLException {
        runReport(connection, "country.Name", country, null);
    }

    // 5. All cities in a district.
    public static void showCitiesByDistrict(
            Connection connection, String district)
            throws SQLException {
        runReport(connection, "city.District", district, null);
    }

    // 6. Top N cities in the world.
    public static void showTopNCitiesWorldwide(
            Connection connection, int n)
            throws SQLException {
        runReport(connection, null, null, n);
    }

    // 7. Top N cities in a continent.
    public static void showTopNCitiesByContinent(
            Connection connection, String continent, int n)
            throws SQLException {
        runReport(connection, "country.Continent", continent, n);
    }

    // 8. Top N cities in a region.
    public static void showTopNCitiesByRegion(
            Connection connection, String region, int n)
            throws SQLException {
        runReport(connection, "country.Region", region, n);
    }

    // 9. Top N cities in a country.
    public static void showTopNCitiesByCountry(
            Connection connection, String country, int n)
            throws SQLException {
        runReport(connection, "country.Name", country, n);
    }

    // 10. Top N cities in a district.
    public static void showTopNCitiesByDistrict(
            Connection connection, String district, int n)
            throws SQLException {
        runReport(connection, "city.District", district, n);
    }
}
