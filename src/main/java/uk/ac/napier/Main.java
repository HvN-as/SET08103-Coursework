
package uk.ac.napier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

    // Main method: Starting point of the application
    public static void main(String[] args) {

        // Read database connection details from environment variables
        String url = System.getenv("DB_URL");
        String username = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // Check that the database URL has been provided
        if (url == null || url.isBlank()) {
            System.err.println(
                    "DB_URL environment variable is not set.");
            System.exit(1);
            return;
        }

        // Check that the database username has been provided
        if (username == null || username.isBlank()) {
            System.err.println(
                    "DB_USER environment variable is not set.");
            System.exit(1);
            return;
        }

        // Check that the database password has been provided
        if (password == null || password.isBlank()) {
            System.err.println(
                    "DB_PASSWORD environment variable is not set.");
            System.exit(1);
            return;
        }

        // Connect to the World database
        // Try-with-resources automatically closes the connection
        try (Connection connection =
                     DriverManager.getConnection(
                             url, username, password)) {

            // Confirm that the database connection was successful
            System.out.println(
                    "Successfully connected to the World database!");

            // Create the capital city reports object
            // Pass the database connection to the reports class
            CapitalCityReports capitalReports =
                    new CapitalCityReports(connection);

            // Create the console menu
            // Pass the reports object, database connection,
            // keyboard input and console output
            ConsoleMenu menu = new ConsoleMenu(
                    capitalReports,
                    connection,
                    System.in,
                    System.out
            );

            // Start the menu so users can select population reports
            // The menu includes capital, city and country reports
            menu.run();

        } catch (SQLException e) {

            // Display an error if the database connection
            // or a database operation fails
            System.err.println(
                    "Database error: " + e.getMessage());

            // Exit the application with an error status
            System.exit(1);
        }
    }
}
