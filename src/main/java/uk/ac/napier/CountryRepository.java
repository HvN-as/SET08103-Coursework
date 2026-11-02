package uk.ac.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryRepository {

    // 1. All countries in the world
    public List<Country> getAllCountries(Connection connection) throws SQLException {
        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                ORDER BY country.Population DESC
                """;

        return executeQuery(connection, sql);
    }

    // 2. All countries in a continent
    public List<Country> getCountriesByContinent(
            Connection connection, String continent) throws SQLException {

        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Continent = ?
                ORDER BY country.Population DESC
                """;

        return executeQuery(connection, sql, continent);
    }

    // 3. All countries in a region
    public List<Country> getCountriesByRegion(
            Connection connection, String region) throws SQLException {

        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Region = ?
                ORDER BY country.Population DESC
                """;

        return executeQuery(connection, sql, region);
    }

    // 4. Top N populated countries in the world
    public List<Country> getTopCountriesWorldwide(
            Connection connection, int limit) throws SQLException {

        validateLimit(limit);

        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                ORDER BY country.Population DESC
                LIMIT ?
                """;

        return executeQuery(connection, sql, limit);
    }

    // 5. Top N populated countries in a continent
    public List<Country> getTopCountriesByContinent(
            Connection connection, String continent, int limit) throws SQLException {

        validateLimit(limit);

        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Continent = ?
                ORDER BY country.Population DESC
                LIMIT ?
                """;

        return executeQuery(connection, sql, continent, limit);
    }

    // 6. Top N populated countries in a region
    public List<Country> getTopCountriesByRegion(
            Connection connection, String region, int limit) throws SQLException {

        validateLimit(limit);

        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Region = ?
                ORDER BY country.Population DESC
                LIMIT ?
                """;

        return executeQuery(connection, sql, region, limit);
    }

    // Checks that the requested number of countries is valid
    private void validateLimit(int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException(
                    "The number of countries must be greater than zero.");
        }
    }

    // Shared method for executing SQL and creating Country objects
    private List<Country> executeQuery(
            Connection connection, String sql, Object... parameters)
            throws SQLException {

        List<Country> countries = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Country country = new Country(
                            resultSet.getString("Code"),
                            resultSet.getString("Name"),
                            resultSet.getString("Continent"),
                            resultSet.getString("Region"),
                            resultSet.getLong("Population"),
                            resultSet.getString("Capital")
                    );

                    countries.add(country);
                }
            }
        }

        return countries;
    }
}