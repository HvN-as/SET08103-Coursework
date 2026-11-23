package uk.ac.napier;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PopulationBreakdownReports {

    // Store the database connection passed from the main application/menu
    private final Connection connection;

    public PopulationBreakdownReports(Connection connection) {
        this.connection = connection;
    }

    // =========================================================================
    // 1. Continent Population Breakdown Report
    // =========================================================================
    public void displayContinentPopulationBreakdown() {
        // Guard clause: check if the database connection is active
        if (connection == null) {
            System.out.println("Database connection is null.");
            return;
        }

        // SQL Query:
        // - Groups countries by continent.
        // - Uses SUM(DISTINCT) on country population to prevent duplicate counting.
        // - Left joins with city to sum up all city populations per continent.
        String query = "SELECT " +
                "country.Continent AS Name, " +
                "SUM(DISTINCT country.Population) AS TotalPopulation, " +
                "SUM(city.Population) AS CityPopulation " +
                "FROM country " +
                "LEFT JOIN city ON country.Code = city.CountryCode " +
                "GROUP BY country.Continent;";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Print report header table formatting
            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("Continent Population Breakdown Report");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("%-15s %-15s %-15s %-15s %-12s %-12s%n",
                    "Continent", "Total Pop", "City Pop", "Outside City", "City %", "Outside %");
            System.out.println("--------------------------------------------------------------------------------");

            // Loop through each row returned by the database query
            while (rs.next()) {
                String continent = rs.getString("Name");
                long totalPop = rs.getLong("TotalPopulation"); // Use long for large world populations
                long cityPop = rs.getLong("CityPopulation");

                // Calculate population living outside cities
                long outsidePop = totalPop - cityPop;

                // Calculate percentages safely (casting to double to avoid integer division truncation)
                double cityPercent = totalPop > 0 ? ((double) cityPop / totalPop) * 100 : 0.0;
                double outsidePercent = totalPop > 0 ? ((double) outsidePop / totalPop) * 100 : 0.0;

                // Print formatted row results
                System.out.printf("%-15s %-15d %-15d %-15d %-11.2f%% %-11.2f%%%n",
                        continent, totalPop, cityPop, outsidePop, cityPercent, outsidePercent);
            }
            System.out.println("--------------------------------------------------------------------------------");

        } catch (SQLException e) {
            // Catch and display any database errors cleanly without crashing the app
            System.out.println("Error executing continent population breakdown query: " + e.getMessage());
        }
    }

    // =========================================================================
    // 2. Region Population Breakdown Report
    // =========================================================================
    public void displayRegionPopulationBreakdown() {
        if (connection == null) {
            System.out.println("Database connection is null.");
            return;
        }

        // SQL Query: Same logic as continent, but grouped by geographic Region
        String query = "SELECT " +
                "country.Region AS Name, " +
                "SUM(DISTINCT country.Population) AS TotalPopulation, " +
                "SUM(city.Population) AS CityPopulation " +
                "FROM country " +
                "LEFT JOIN city ON country.Code = city.CountryCode " +
                "GROUP BY country.Region;";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("Region Population Breakdown Report");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("%-30s %-15s %-15s %-15s %-12s %-12s%n",
                    "Region", "Total Pop", "City Pop", "Outside City", "City %", "Outside %");
            System.out.println("--------------------------------------------------------------------------------");

            while (rs.next()) {
                String region = rs.getString("Name");
                long totalPop = rs.getLong("TotalPopulation");
                long cityPop = rs.getLong("CityPopulation");
                long outsidePop = totalPop - cityPop;

                double cityPercent = totalPop > 0 ? ((double) cityPop / totalPop) * 100 : 0.0;
                double outsidePercent = totalPop > 0 ? ((double) outsidePop / totalPop) * 100 : 0.0;

                System.out.printf("%-30s %-15d %-15d %-15d %-11.2f%% %-11.2f%%%n",
                        region, totalPop, cityPop, outsidePop, cityPercent, outsidePercent);
            }
            System.out.println("--------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error executing region population breakdown query: " + e.getMessage());
        }
    }

    // =========================================================================
    // 3. Country Population Breakdown Report
    // =========================================================================
    public void displayCountryPopulationBreakdown() {
        if (connection == null) {
            System.out.println("Database connection is null.");
            return;
        }

        // SQL Query: Evaluates individual countries and sums up their cities
        String query = "SELECT " +
                "country.Name AS Name, " +
                "country.Population AS TotalPopulation, " +
                "SUM(city.Population) AS CityPopulation " +
                "FROM country " +
                "LEFT JOIN city ON country.Code = city.CountryCode " +
                "GROUP BY country.Code, country.Name, country.Population;";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("--------------------------------------------------------------------------------");
            System.out.println("Country Population Breakdown Report");
            System.out.println("--------------------------------------------------------------------------------");
            System.out.printf("%-30s %-15s %-15s %-15s %-12s %-12s%n",
                    "Country", "Total Pop", "City Pop", "Outside City", "City %", "Outside %");
            System.out.println("--------------------------------------------------------------------------------");

            while (rs.next()) {
                String country = rs.getString("Name");
                long totalPop = rs.getLong("TotalPopulation");
                long cityPop = rs.getLong("CityPopulation");
                long outsidePop = totalPop - cityPop;

                double cityPercent = totalPop > 0 ? ((double) cityPop / totalPop) * 100 : 0.0;
                double outsidePercent = totalPop > 0 ? ((double) outsidePop / totalPop) * 100 : 0.0;

                System.out.printf("%-30s %-15d %-15d %-15d %-11.2f%% %-11.2f%%%n",
                        country, totalPop, cityPop, outsidePop, cityPercent, outsidePercent);
            }
            System.out.println("--------------------------------------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("Error executing country population breakdown query: " + e.getMessage());
        }
    }
}
