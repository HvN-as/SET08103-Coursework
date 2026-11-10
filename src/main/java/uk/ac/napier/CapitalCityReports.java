
package uk.ac.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Generates six population reports for capital cities
 * using the MySQL World database.
 *
 * The reports allow users to view capitals worldwide,
 * by continent, by region, or as Top N results.
 *
 * JDBC is used to communicate with the database.
 */
public class CapitalCityReports {

    // Store the database connection used by all reports.
    // final means this reference cannot be reassigned.
    private final Connection connection;

    /**
     * Constructor: receives an existing database connection.
     *
     * @param connection the active JDBC database connection
     */
    public CapitalCityReports(Connection connection) {
        this.connection = connection;
    }

    /**
     * Report 1: Displays all capital cities worldwide.
     *
     * Retrieves capital names, countries and populations,
     * ordered from highest to lowest population.
     *
     * @throws SQLException if the database query fails
     */
    public void allCapitalsWorldwide() throws SQLException {

        // JOIN connects each country to its capital city.
        // country.Capital contains the ID of the capital.
        // DESC sorts the population from largest to smallest.
        String sql = """
                SELECT city.Name AS Capital,
                       country.Name AS Country,
                       city.Population AS Population
                FROM country
                JOIN city ON country.Capital = city.ID
                ORDER BY city.Population DESC
                """;

        // PreparedStatement executes the SQL query.
        // try-with-resources automatically closes the
        // statement and ResultSet after use.
        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            // Display the column headings.
            printHeader();

            // Process every row returned by MySQL.
            // next() returns false when no rows remain.
            while (results.next()) {
                printRow(results);
            }
        }
    }

    /**
     * Report 2: Displays capital cities in a continent.
     *
     * @param continent the continent selected by the user
     * @throws IllegalArgumentException if continent is null or blank
     * @throws SQLException if the database query fails
     */
    public void capitalsByContinent(String continent)
            throws SQLException {

        // Reject invalid continent input before querying.
        // || means OR, so either condition causes rejection.
        if (continent == null || continent.isBlank()) {
            throw new IllegalArgumentException(
                    "Continent cannot be blank.");
        }

        // WHERE filters the countries by continent.
        // The ? placeholder receives the user's input.
        String sql = """
                SELECT city.Name AS Capital,
                       country.Name AS Country,
                       city.Population AS Population
                FROM country
                JOIN city ON country.Capital = city.ID
                WHERE country.Continent = ?
                ORDER BY city.Population DESC
                """;

        // Create a parameterised SQL statement.
        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Bind the continent to the first placeholder.
            // This helps prevent SQL injection.
            statement.setString(1, continent);

            // Execute the query and retrieve matching rows.
            try (ResultSet results = statement.executeQuery()) {

                System.out.println(
                        "\nCapital cities in " + continent);

                printHeader();

                // Display each matching capital city.
                while (results.next()) {
                    printRow(results);
                }
            }
        }
    }

    /**
     * Report 3: Displays capital cities in a region.
     *
     * @param region the geographical region selected
     * @throws IllegalArgumentException if region is null or blank
     * @throws SQLException if the database query fails
     */
    public void capitalsByRegion(String region)
            throws SQLException {

        // Prevent null or blank region values.
        if (region == null || region.isBlank()) {
            throw new IllegalArgumentException(
                    "Region cannot be blank.");
        }

        // WHERE filters by region instead of continent.
        // JOIN identifies each country's capital city.
        String sql = """
                SELECT city.Name AS Capital,
                       country.Name AS Country,
                       city.Population AS Population
                FROM country
                JOIN city ON country.Capital = city.ID
                WHERE country.Region = ?
                ORDER BY city.Population DESC
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Replace the first ? with the region name.
            statement.setString(1, region);

            try (ResultSet results = statement.executeQuery()) {

                System.out.println(
                        "\nCapital cities in region: " + region);

                printHeader();

                // Read and print each matching record.
                while (results.next()) {
                    printRow(results);
                }
            }
        }
    }

    /**
     * Report 4: Displays the Top N capital cities worldwide.
     *
     * @param topN maximum number of capitals to display
     * @throws IllegalArgumentException if topN is not positive
     * @throws SQLException if the database query fails
     */
    public void topCapitalsWorldwide(int topN)
            throws SQLException {

        // A Top N report requires a positive integer.
        // Zero and negative numbers are rejected.
        if (topN <= 0) {
            throw new IllegalArgumentException(
                    "Top N must be greater than zero.");
        }

        // ORDER BY ranks capitals by population.
        // LIMIT restricts the number of returned rows.
        String sql = """
                SELECT city.Name AS Capital,
                       country.Name AS Country,
                       city.Population AS Population
                FROM country
                JOIN city ON country.Capital = city.ID
                ORDER BY city.Population DESC
                LIMIT ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // setInt binds an integer to the first ?.
            // JDBC parameter numbering starts at 1.
            statement.setInt(1, topN);

            try (ResultSet results = statement.executeQuery()) {

                System.out.println(
                        "\nTop " + topN +
                                " capital cities worldwide");

                printHeader();

                // Print up to topN returned records.
                while (results.next()) {
                    printRow(results);
                }
            }
        }
    }

    /**
     * Report 5: Displays the Top N capital cities
     * within a selected continent.
     *
     * @param continent the selected continent
     * @param topN maximum number of capitals to display
     * @throws IllegalArgumentException if either input is invalid
     * @throws SQLException if the database query fails
     */
    public void topCapitalsByContinent(String continent, int topN)
            throws SQLException {

        // Validate the continent name.
        if (continent == null || continent.isBlank()) {
            throw new IllegalArgumentException(
                    "Continent cannot be blank.");
        }

        // Validate the requested number of results.
        if (topN <= 0) {
            throw new IllegalArgumentException(
                    "Top N must be greater than zero.");
        }

        // WHERE filters by continent.
        // ORDER BY sorts populations in descending order.
        // LIMIT returns at most the requested number of rows.
        String sql = """
                SELECT city.Name AS Capital,
                       country.Name AS Country,
                       city.Population AS Population
                FROM country
                JOIN city ON country.Capital = city.ID
                WHERE country.Continent = ?
                ORDER BY city.Population DESC
                LIMIT ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // First ? receives the continent name.
            statement.setString(1, continent);

            // Second ? receives the result limit.
            statement.setInt(2, topN);

            try (ResultSet results = statement.executeQuery()) {

                System.out.println(
                        "\nTop " + topN +
                                " capital cities in " + continent);

                printHeader();

                // Display each matching capital city.
                while (results.next()) {
                    printRow(results);
                }
            }
        }
    }

    /**
     * Report 6: Displays the Top N capital cities
     * within a selected geographical region.
     *
     * @param region the selected geographical region
     * @param topN maximum number of capitals to display
     * @throws IllegalArgumentException if either input is invalid
     * @throws SQLException if the database query fails
     */
    public void topCapitalsByRegion(String region, int topN)
            throws SQLException {

        // Validate the region before executing SQL.
        if (region == null || region.isBlank()) {
            throw new IllegalArgumentException(
                    "Region cannot be blank.");
        }

        // Reject zero and negative result limits.
        if (topN <= 0) {
            throw new IllegalArgumentException(
                    "Top N must be greater than zero.");
        }

        // JOIN connects countries to capital cities.
        // WHERE restricts the results to the chosen region.
        // ORDER BY ranks capitals by population.
        // LIMIT restricts the number of results.
        String sql = """
                SELECT city.Name AS Capital,
                       country.Name AS Country,
                       city.Population AS Population
                FROM country
                JOIN city ON country.Capital = city.ID
                WHERE country.Region = ?
                ORDER BY city.Population DESC
                LIMIT ?
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Bind the region to the first placeholder.
            statement.setString(1, region);

            // Bind the maximum number of results to the second.
            statement.setInt(2, topN);

            try (ResultSet results = statement.executeQuery()) {

                System.out.println(
                        "\nTop " + topN +
                                " capital cities in region: " + region);

                printHeader();

                // Iterate through the returned rows.
                while (results.next()) {
                    printRow(results);
                }
            }
        }
    }

    /**
     * Prints the column headings for all six reports.
     *
     * This helper avoids duplicating formatting code.
     */
    private void printHeader() {

        // printf produces aligned columns.
        // %-35s left-aligns text in a field of at least 35 characters.
        // %-15s left-aligns text in a field of at least 15 characters.
        // %n moves to a new line.
        System.out.printf("%-35s %-35s %-15s%n",
                "Capital", "Country", "Population");
    }

    /**
     * Prints one capital city from the ResultSet.
     *
     * @param results the current database result row
     * @throws SQLException if reading a column fails
     */
    private void printRow(ResultSet results)
            throws SQLException {

        // Read the SQL columns using their aliases.
        // getString reads the capital and country names.
        // getLong reads the population as a whole number.
        // %-15d formats the population as a decimal integer.
        System.out.printf("%-35s %-35s %-15d%n",
                results.getString("Capital"),
                results.getString("Country"),
                results.getLong("Population"));
    }

} // End of CapitalCityReports class
