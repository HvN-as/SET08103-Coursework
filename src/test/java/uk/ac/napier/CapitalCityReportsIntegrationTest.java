
package uk.ac.napier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class CapitalCityReportsIntegrationTest {

    @Test
    @EnabledIfEnvironmentVariable(
            named = "RUN_DB_INTEGRATION_TESTS",
            matches = "true"
    )
    void testWorldDatabaseConnection() throws Exception {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        assertNotNull(url);
        assertNotNull(user);
        assertNotNull(password);

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            assertFalse(connection.isClosed());

            try (Statement statement = connection.createStatement();
                 ResultSet results = statement.executeQuery(
                         "SELECT COUNT(*) FROM country")) {

                assertTrue(results.next());
                assertEquals(239, results.getInt(1));
            }
        }
    }

    @Test
    @EnabledIfEnvironmentVariable(
            named = "RUN_DB_INTEGRATION_TESTS",
            matches = "true"
    )
    void testTopFiveCapitalCitiesWorldwide() throws Exception {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            CapitalCityReports reports =
                    new CapitalCityReports(connection);

            ByteArrayOutputStream output =
                    new ByteArrayOutputStream();

            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(
                        output, true, StandardCharsets.UTF_8));

                reports.topCapitalsWorldwide(5);

            } finally {
                System.setOut(originalOut);
            }

            String result =
                    output.toString(StandardCharsets.UTF_8);

            assertTrue(result.contains("Seoul"));
            assertTrue(result.contains("Jakarta"));
            assertTrue(result.contains("Ciudad de México"));
            assertTrue(result.contains("Moscow"));
            assertTrue(result.contains("Tokyo"));

            assertFalse(result.contains("Peking"));
        }
    }

    @Test
    @EnabledIfEnvironmentVariable(
            named = "RUN_DB_INTEGRATION_TESTS",
            matches = "true"
    )
    void testCapitalCitiesInEurope() throws Exception {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            CapitalCityReports reports =
                    new CapitalCityReports(connection);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(
                        output, true, StandardCharsets.UTF_8));

                reports.capitalsByContinent("Europe");

            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString(StandardCharsets.UTF_8);

            assertTrue(result.contains("London"));
            assertTrue(result.contains("Paris"));
            assertTrue(result.contains("Berlin"));
            assertFalse(result.contains("Tokyo"));
        }
    }

    @Test
    @EnabledIfEnvironmentVariable(
            named = "RUN_DB_INTEGRATION_TESTS",
            matches = "true"
    )
    void testCapitalCitiesInWesternEurope() throws Exception {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            CapitalCityReports reports =
                    new CapitalCityReports(connection);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(
                        output, true, StandardCharsets.UTF_8));

                reports.capitalsByRegion("Western Europe");

            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString(StandardCharsets.UTF_8);

            assertTrue(result.contains("Berlin"));
            assertTrue(result.contains("Paris"));
            assertTrue(result.contains("Amsterdam"));
            assertFalse(result.contains("Tokyo"));
        }
    }

    @Test
    @EnabledIfEnvironmentVariable(
            named = "RUN_DB_INTEGRATION_TESTS",
            matches = "true"
    )
    void testTopFiveCapitalCitiesInEurope() throws Exception {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            CapitalCityReports reports =
                    new CapitalCityReports(connection);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(
                        output, true, StandardCharsets.UTF_8));

                reports.topCapitalsByContinent("Europe", 5);

            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString(StandardCharsets.UTF_8);

            assertTrue(result.contains("Moscow"));
            assertTrue(result.contains("London"));
            assertTrue(result.contains("Berlin"));
            assertTrue(result.contains("Madrid"));
            assertTrue(result.contains("Roma"));
            assertFalse(result.contains("Tokyo"));
        }
    }

    @Test
    @EnabledIfEnvironmentVariable(
            named = "RUN_DB_INTEGRATION_TESTS",
            matches = "true"
    )
    void testTopFiveCapitalCitiesInWesternEurope() throws Exception {

        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            CapitalCityReports reports =
                    new CapitalCityReports(connection);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(
                        output, true, StandardCharsets.UTF_8));

                reports.topCapitalsByRegion("Western Europe", 5);

            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString(StandardCharsets.UTF_8);

            assertTrue(result.contains("Berlin"));
            assertTrue(result.contains("Paris"));
            assertTrue(result.contains("Wien"));
            assertTrue(result.contains("Amsterdam"));
            assertTrue(result.contains("Bruxelles"));
            assertFalse(result.contains("Tokyo"));
        }
    }

}
