
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

            assertTrue(result.contains("Tokyo"));
            assertTrue(result.contains("Osaka"));
            assertTrue(result.contains("Mumbai"));
            assertTrue(result.contains("Delhi"));

            assertFalse(result.contains("London"));
            assertFalse(result.contains("Manchester"));
            assertFalse(result.contains("New York"));
            assertFalse(result.contains("Los Angeles"));

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

            assertTrue(result.contains("Tokyo"));
            assertTrue(result.contains("Osaka"));

            assertFalse(result.contains("Mumbai"));
            assertFalse(result.contains("Delhi"));
            assertFalse(result.contains("London"));
            assertFalse(result.contains("New York"));

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

            assertTrue(result.contains("Tokyo"));
            assertTrue(result.contains("Osaka"));

            assertFalse(result.contains("Mumbai"));
            assertFalse(result.contains("Delhi"));
            assertFalse(result.contains("London"));
            assertFalse(result.contains("New York"));

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

    @Test
    void allCitiesWorldwideReturnsEveryCityInPopulationOrder()
            throws Exception {

        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));
                CityReport.showAllCities(connection);
            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            String[] expectedCities = {
                    "Mumbai", "Delhi", "Tokyo", "New York",
                    "London", "Los Angeles", "Osaka", "Manchester"
            };

            int previousPosition = -1;

            for (String city : expectedCities) {
                int position = result.indexOf(city);

                assertTrue(position >= 0,
                        city + " should appear in the report");

                assertTrue(position > previousPosition,
                        city + " should appear in descending population order");

                previousPosition = position;
            }
        }
    }

    @Test
    void topTwoCitiesByContinentReturnsCorrectCities()
            throws Exception {

        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));
                CityReport.showTopNCitiesByContinent(connection, "Asia", 2);
            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            assertTrue(result.contains("Mumbai"));
            assertTrue(result.contains("Delhi"));

            assertFalse(result.contains("Tokyo"));
            assertFalse(result.contains("Osaka"));
            assertFalse(result.contains("London"));

            assertTrue(result.indexOf("Mumbai") < result.indexOf("Delhi"));
        }
    }

    @Test
    void topOneCityByRegionReturnsTokyoOnly() throws Exception {
        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));
                CityReport.showTopNCitiesByRegion(
                        connection, "Eastern Asia", 1);
            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            assertTrue(result.contains("Tokyo"));
            assertFalse(result.contains("Osaka"));
            assertFalse(result.contains("Mumbai"));
            assertFalse(result.contains("London"));
            assertTrue(result.contains("9000000"));
        }
    }

    @Test
    void topOneCityByCountryReturnsTokyoOnly() throws Exception {
        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));
                CityReport.showTopNCitiesByCountry(
                        connection, "Japan", 1);
            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            assertTrue(result.contains("Tokyo"));
            assertTrue(result.contains("9000000"));

            assertFalse(result.contains("Osaka"));
            assertFalse(result.contains("Mumbai"));
            assertFalse(result.contains("London"));
        }
    }

    @Test
    void topOneCityByDistrictReturnsLondonOnly() throws Exception {
        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));
                CityReport.showTopNCitiesByDistrict(connection, "England", 1);
            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            assertTrue(result.contains("London"));
            assertTrue(result.contains("8000000"));
            assertFalse(result.contains("Manchester"));
            assertFalse(result.contains("Tokyo"));
        }
    }

    @Test
    void topNLargerThanDatasetReturnsAllMatchingCities() throws Exception {
        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));
                CityReport.showTopNCitiesByCountry(connection, "Japan", 100);
            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            assertTrue(result.contains("Tokyo"));
            assertTrue(result.contains("Osaka"));
            assertFalse(result.contains("Mumbai"));
            assertFalse(result.contains("London"));
        }
    }

    @Test
    void unknownContinentReturnsNoCities() throws Exception {
        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));
                CityReport.showCitiesByContinent(connection, "Atlantis");
            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            assertTrue(result.contains("Name"));
            assertTrue(result.contains("Population"));
            assertFalse(result.contains("Tokyo"));
            assertFalse(result.contains("London"));
            assertFalse(result.contains("Mumbai"));
        }
    }

    @Test
    void unknownRegionReturnsNoCities() throws Exception {
        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));
                CityReport.showCitiesByRegion(connection, "Unknown Region");
            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            assertTrue(result.contains("Name"));
            assertTrue(result.contains("Population"));
            assertFalse(result.contains("Tokyo"));
            assertFalse(result.contains("Mumbai"));
        }
    }

    @Test
    void unknownDistrictReturnsNoCities() throws Exception {
        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));
                CityReport.showCitiesByDistrict(connection, "Unknown District");
            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            assertTrue(result.contains("Name"));
            assertTrue(result.contains("Population"));
            assertFalse(result.contains("London"));
            assertFalse(result.contains("Manchester"));
        }
    }

    @Test
    void worldwideReportOrdersCitiesByPopulationDescending() throws Exception {
        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));
                CityReport.showAllCities(connection);
            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();

            assertTrue(result.indexOf("Mumbai") < result.indexOf("Delhi"));
            assertTrue(result.indexOf("Delhi") < result.indexOf("Tokyo"));
            assertTrue(result.indexOf("Tokyo") < result.indexOf("New York"));
            assertTrue(result.indexOf("New York") < result.indexOf("London"));
            assertTrue(result.indexOf("London") < result.indexOf("Los Angeles"));
            assertTrue(result.indexOf("Los Angeles") < result.indexOf("Osaka"));
            assertTrue(result.indexOf("Osaka") < result.indexOf("Manchester"));
        }
    }

    @Test
    void cityReportDisplaysRequiredColumnsAndPopulation() throws Exception {
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

            assertTrue(result.contains("Name"));
            assertTrue(result.contains("Country"));
            assertTrue(result.contains("District"));
            assertTrue(result.contains("Population"));

            assertTrue(result.contains("Tokyo"));
            assertTrue(result.contains("Japan"));
            assertTrue(result.contains("9000000"));
            assertTrue(result.contains("2700000"));
        }
    }

    @Test
    void worldwideReportDoesNotDuplicateCities() throws Exception {
        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;

            try {
                System.setOut(new PrintStream(output));
                CityReport.showAllCities(connection);
            } finally {
                System.setOut(originalOut);
            }

            String result = output.toString();
            String[] lines = result.strip().split("\\R");

            // One header plus eight city rows
            assertEquals(9, lines.length,
                    "The report should contain exactly eight city rows");
        }
    }

    @Test
    void citiesWithSameNameInDifferentCountriesAreBothReported()
            throws Exception {

        String url = System.getenv("TEST_DB_URL");
        String user = System.getenv("TEST_DB_USER");
        String password = System.getenv("TEST_DB_PASSWORD");

        try (Connection connection =
                     DriverManager.getConnection(url, user, password)) {

            connection.setAutoCommit(false);

            try {
                try (java.sql.PreparedStatement statement =
                             connection.prepareStatement(
                                     "INSERT INTO city " +
                                             "(Name, CountryCode, District, Population) " +
                                             "VALUES (?, ?, ?, ?)")) {

                    statement.setString(1, "London");
                    statement.setString(2, "GBR");
                    statement.setString(3, "Shared District");
                    statement.setInt(4, 150000);
                    statement.executeUpdate();

                    statement.setString(1, "London");
                    statement.setString(2, "USA");
                    statement.setString(3, "Shared District");
                    statement.setInt(4, 250000);
                    statement.executeUpdate();
                }

                ByteArrayOutputStream output = new ByteArrayOutputStream();
                PrintStream originalOut = System.out;

                try {
                    System.setOut(new PrintStream(output));

                    CityReport.showCitiesByDistrict(
                            connection, "Shared District");

                } finally {
                    System.setOut(originalOut);
                }

                String result = output.toString();

                // Both cities must appear, despite sharing a name.
                assertEquals(2,
                        result.split("London", -1).length - 1);

                // Both countries must appear.
                assertTrue(result.contains("United Kingdom"));
                assertTrue(result.contains("United States"));

                // Both populations must appear.
                assertTrue(result.contains("150000"));
                assertTrue(result.contains("250000"));

                // Higher population must appear first.
                assertTrue(result.indexOf("250000")
                        < result.indexOf("150000"));

            } finally {
                connection.rollback();
            }
        }
    }

}
