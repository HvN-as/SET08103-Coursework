
package uk.ac.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryReport {

    // REPORT 1: Display all countries in the world
    public static void displayCountries() {

        // Get all countries, sorted by population
        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital,
                       city.Population AS CapitalPopulation
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                ORDER BY country.Population DESC
                """;

        // Run the query without any filters
        runReport(sql, null, 0);
    }


    // REPORT 2: Display countries from a continent
    public static void displayCountriesByContinent(String continent) {

        // The ? will be replaced with the continent name
        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital,
                       city.Population AS CapitalPopulation
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Continent = ?
                ORDER BY country.Population DESC
                """;

        // Run the query using the continent entered
        runReport(sql, continent, 0);
    }


    // REPORT 3: Display countries from a region
    public static void displayCountriesByRegion(String region) {

        // The ? will be replaced with the region name
        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital,
                       city.Population AS CapitalPopulation
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Region = ?
                ORDER BY country.Population DESC
                """;

        // Run the query using the region entered
        runReport(sql, region, 0);
    }


    // REPORT 4: Display the top N countries worldwide
    public static void displayTopCountries(int number) {

        // LIMIT ? controls how many countries are returned
        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital,
                       city.Population AS CapitalPopulation
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                ORDER BY country.Population DESC
                LIMIT ?
                """;

        // Run the query using the number entered
        runReport(sql, null, number);
    }


    // REPORT 5: Display the top N countries in a continent
    public static void displayTopCountriesByContinent(
            String continent, int number) {

        // Filter by continent and limit the number of results
        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital,
                       city.Population AS CapitalPopulation
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Continent = ?
                ORDER BY country.Population DESC
                LIMIT ?
                """;

        // Run the query with the continent and number
        runReport(sql, continent, number);
    }


    // REPORT 6: Display the top N countries in a region
    public static void displayTopCountriesByRegion(
            String region, int number) {

        // Filter by region and limit the number of results
        String sql = """
                SELECT country.Code, country.Name, country.Continent,
                       country.Region, country.Population,
                       city.Name AS Capital,
                       city.Population AS CapitalPopulation
                FROM country
                LEFT JOIN city ON country.Capital = city.ID
                WHERE country.Region = ?
                ORDER BY country.Population DESC
                LIMIT ?
                """;

        // Run the query with the region and number
        runReport(sql, region, number);
    }


    // This method runs the SQL query and displays the results
    // All six reports use this method to avoid repeating code
    private static void runReport(
            String sql, String location, int number) {

        // Open a connection to the database
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the continent or region if one was provided
            if (location != null) {
                statement.setString(1, location);
            }

            // Set the number of countries if a limit was provided
            if (number > 0) {

                // If there is a location, LIMIT is the second ?
                if (location != null) {
                    statement.setInt(2, number);
                } else {
                    // Otherwise LIMIT is the first ?
                    statement.setInt(1, number);
                }
            }

            // Run the SQL query
            try (ResultSet results = statement.executeQuery()) {

                // Print the table headings
                System.out.printf(
                        "%-5s %-35s %-15s %-30s %15s %-30s %20s%n",
                        "Code", "Country", "Continent", "Region",
                        "Population", "Capital", "Capital Population");

                // Print a line under the headings
                System.out.println("-".repeat(160));

                // Check if any countries were found
                boolean found = false;

                // Loop through each country returned
                while (results.next()) {

                    found = true;

                    // Get the capital city name
                    String capital = results.getString("Capital");

                    // Use N/A if the capital is missing
                    if (capital == null) {
                        capital = "N/A";
                    }

                    // Get the capital city population
                    long capitalPopulation =
                            results.getLong("CapitalPopulation");

                    // Check whether the capital population is missing
                    boolean noCapitalPopulation = results.wasNull();

                    // Display N/A if no capital population is recorded
                    String capitalPopulationText = noCapitalPopulation
                            ? "N/A"
                            : String.format("%,d", capitalPopulation);

                    // Print the country details
                    System.out.printf(
                            "%-5s %-35s %-15s %-30s %,15d %-30s %20s%n",
                            results.getString("Code"),
                            results.getString("Name"),
                            results.getString("Continent"),
                            results.getString("Region"),
                            results.getLong("Population"),
                            capital,
                            capitalPopulationText
                    );
                }

                // Show a message if no countries were returned
                if (!found) {
                    System.out.println("No countries found.");
                }
            }

        } catch (SQLException e) {

            // Display an error if the query fails
            System.err.println(
                    "Could not generate country report: "
                            + e.getMessage());
        }
    }
}
