package uk.ac.napier;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;
public class CityReportIntegrationTest {

    @Test
    void connectsToTestDatabase() throws SQLException {
        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        if (url == null || user == null || password == null) {
            throw new IllegalStateException(
                    "Test database credentials are not configured."
            );
        }

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            assertTrue(connection.isValid(5));
        }
    }

    @Test
    void topFiveCitiesMatchExpectedResults() throws Exception {

        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));

                CityReport.showTopNCitiesWorldwide(connection, 5);

            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            assertTrue(result.contains("Mumbai"));
            assertTrue(result.contains("Delhi"));
            assertTrue(result.contains("Tokyo"));
            assertTrue(result.contains("New York"));
            assertTrue(result.contains("London"));

            assertFalse(result.contains("Los Angeles"));
            assertFalse(result.contains("Manchester"));

            assertTrue(result.indexOf("Mumbai") < result.indexOf("Delhi"));
            assertTrue(result.indexOf("Delhi") < result.indexOf("Tokyo"));
            assertTrue(result.indexOf("Tokyo") < result.indexOf("New York"));
            assertTrue(result.indexOf("New York") < result.indexOf("London"));
        }
    }
    @Test
    void citiesByContinentOnlyReturnsAsianCities() throws Exception {

        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));

                CityReport.showCitiesByContinent(connection, "Asia");

            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            // Asian cities should appear
            assertTrue(result.contains("Tokyo"));
            assertTrue(result.contains("Osaka"));
            assertTrue(result.contains("Mumbai"));
            assertTrue(result.contains("Delhi"));

            // Cities outside Asia should not appear
            assertFalse(result.contains("London"));
            assertFalse(result.contains("Manchester"));
            assertFalse(result.contains("New York"));
            assertFalse(result.contains("Los Angeles"));

            // Verify descending population order
            assertTrue(result.indexOf("Mumbai") < result.indexOf("Delhi"));
            assertTrue(result.indexOf("Delhi") < result.indexOf("Tokyo"));
            assertTrue(result.indexOf("Tokyo") < result.indexOf("Osaka"));
        }

    }
    @Test
    void citiesByCountryOnlyReturnsJapaneseCities() throws Exception {

        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));

                CityReport.showCitiesByCountry(connection, "Japan");

            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            // Japanese cities must appear
            assertTrue(result.contains("Tokyo"));
            assertTrue(result.contains("Osaka"));

            // Cities from other countries must not appear
            assertFalse(result.contains("Mumbai"));
            assertFalse(result.contains("Delhi"));
            assertFalse(result.contains("London"));
            assertFalse(result.contains("New York"));

            // Tokyo has a larger population than Osaka
            assertTrue(result.indexOf("Tokyo") < result.indexOf("Osaka"));
        }
    }
    @Test
    void citiesByRegionOnlyReturnsEasternAsianCities() throws Exception {

        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));

                CityReport.showCitiesByRegion(connection, "Eastern Asia");

            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            // Cities in Eastern Asia
            assertTrue(result.contains("Tokyo"));
            assertTrue(result.contains("Osaka"));

            // Cities outside Eastern Asia
            assertFalse(result.contains("Mumbai"));
            assertFalse(result.contains("Delhi"));
            assertFalse(result.contains("London"));
            assertFalse(result.contains("New York"));

            // Verify descending population order
            assertTrue(result.indexOf("Tokyo") < result.indexOf("Osaka"));
        }
    }
    @Test
    void citiesByDistrictOnlyReturnsEnglishCities() throws Exception {

        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));

                CityReport.showCitiesByDistrict(connection, "England");

            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            assertTrue(result.contains("London"));
            assertTrue(result.contains("Manchester"));

            assertFalse(result.contains("Tokyo"));
            assertFalse(result.contains("Mumbai"));
            assertFalse(result.contains("New York"));

            assertTrue(result.indexOf("London") < result.indexOf("Manchester"));
        }
    }
    @Test
    void unknownCountryReturnsNoCities() throws Exception {

        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));

                CityReport.showCitiesByCountry(connection, "Atlantis");

            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            assertTrue(result.contains("Name"));
            assertTrue(result.contains("Country"));
            assertTrue(result.contains("District"));
            assertTrue(result.contains("Population"));

            assertFalse(result.contains("Tokyo"));
            assertFalse(result.contains("Mumbai"));
            assertFalse(result.contains("London"));
            assertFalse(result.contains("New York"));
        }
    }
}