package uk.ac.napier;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.verify;


public class CityReportTest {

    @Test
    void rejectsZeroTopN() {

        Connection connection = mock(Connection.class);

        assertThrows(IllegalArgumentException.class, () -> {
            CityReport.showTopNCitiesWorldwide(connection, 0);
        });
    }

    @Test
    void rejectsNegativeTopN() {
        Connection connection = mock(Connection.class);

        assertThrows(IllegalArgumentException.class,
                () -> CityReport.showTopNCitiesWorldwide(connection, -5));
    }

    @Test
    void rejectsZeroTopNByContinent() {
        Connection connection = mock(Connection.class);

        assertThrows(IllegalArgumentException.class,
                () -> CityReport.showTopNCitiesByContinent(
                        connection, "Asia", 0));
    }
    @Test
    void rejectsNegativeTopNByContinent() {
        Connection connection = mock(Connection.class);

        assertThrows(IllegalArgumentException.class,
                () -> CityReport.showTopNCitiesByContinent(
                        connection, "Asia", -5));
    }
    @Test
    void displaysCorrectColumnHeadings() throws Exception {

        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet results = mock(ResultSet.class);

        when(connection.prepareStatement(
                org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(results);
        when(results.next()).thenReturn(false);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(output));

            CityReport.showAllCities(connection);

        } finally {
            System.setOut(originalOutput);
        }

        String report = output.toString();

        assertTrue(report.contains("Name"));
        assertTrue(report.contains("Country"));
        assertTrue(report.contains("District"));
        assertTrue(report.contains("Population"));
    }

    @Test
    void displaysCityDataCorrectly() throws Exception {

        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet results = mock(ResultSet.class);

        when(connection.prepareStatement(
                org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(results);

        // Simulate one city being returned
        when(results.next()).thenReturn(true, false);

        when(results.getString("Name")).thenReturn("Tokyo");
        when(results.getString("Country")).thenReturn("Japan");
        when(results.getString("District")).thenReturn("Tokyo-to");
        when(results.getLong("Population")).thenReturn(8336599L);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOutput = System.out;

        try {
            System.setOut(new PrintStream(output));

            CityReport.showAllCities(connection);

        } finally {
            System.setOut(originalOutput);
        }

        String report = output.toString();

        assertTrue(report.contains("Tokyo"));
        assertTrue(report.contains("Japan"));
        assertTrue(report.contains("Tokyo-to"));
        assertTrue(report.contains("8336599"));
    }
    @Test
    void ordersCitiesByPopulationDescending() throws Exception {

        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet results = mock(ResultSet.class);

        when(connection.prepareStatement(
                org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(results);

        CityReport.showAllCities(connection);

        ArgumentCaptor<String> sqlCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(connection).prepareStatement(sqlCaptor.capture());

        String sql = sqlCaptor.getValue();

        assertTrue(sql.contains("ORDER BY city.Population DESC"));
    }
    @Test
    void filtersCitiesByContinent() throws Exception {

        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet results = mock(ResultSet.class);

        when(connection.prepareStatement(
                org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(results);

        CityReport.showCitiesByContinent(connection, "Asia");

        ArgumentCaptor<String> sqlCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(connection).prepareStatement(sqlCaptor.capture());

        String sql = sqlCaptor.getValue();

        assertTrue(sql.contains("WHERE country.Continent = ?"));

        verify(statement).setString(1, "Asia");
    }
    @Test
    void filtersCitiesByCountry() throws Exception {

        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet results = mock(ResultSet.class);

        when(connection.prepareStatement(
                org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(results);

        CityReport.showCitiesByCountry(connection, "Japan");

        ArgumentCaptor<String> sqlCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(connection).prepareStatement(sqlCaptor.capture());

        String sql = sqlCaptor.getValue();

        assertTrue(sql.contains("WHERE country.Name = ?"));

        verify(statement).setString(1, "Japan");
    }
    @Test
    void filtersCitiesByRegion() throws Exception {

        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet results = mock(ResultSet.class);

        when(connection.prepareStatement(
                org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(results);

        CityReport.showCitiesByRegion(connection, "Eastern Asia");

        ArgumentCaptor<String> sqlCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(connection).prepareStatement(sqlCaptor.capture());

        String sql = sqlCaptor.getValue();

        assertTrue(sql.contains("WHERE country.Region = ?"));

        verify(statement).setString(1, "Eastern Asia");
    }
    @Test
    void filtersCitiesByDistrict() throws Exception {

        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet results = mock(ResultSet.class);

        when(connection.prepareStatement(
                org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(results);

        CityReport.showCitiesByDistrict(connection, "California");

        ArgumentCaptor<String> sqlCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(connection).prepareStatement(sqlCaptor.capture());

        String sql = sqlCaptor.getValue();

        assertTrue(sql.contains("WHERE city.District = ?"));

        verify(statement).setString(1, "California");
    }
    @Test
    void limitsWorldwideCitiesToTopN() throws Exception {

        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet results = mock(ResultSet.class);

        when(connection.prepareStatement(
                org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(results);

        CityReport.showTopNCitiesWorldwide(connection, 5);

        ArgumentCaptor<String> sqlCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(connection).prepareStatement(sqlCaptor.capture());

        String sql = sqlCaptor.getValue();

        assertTrue(sql.contains("ORDER BY city.Population DESC"));
        assertTrue(sql.contains("LIMIT ?"));

        verify(statement).setInt(1, 5);
    }
    @Test
    void limitsCitiesByContinentToTopN() throws Exception {

        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet results = mock(ResultSet.class);

        when(connection.prepareStatement(
                org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(statement);

        when(statement.executeQuery()).thenReturn(results);

        CityReport.showTopNCitiesByContinent(connection, "Asia", 5);

        ArgumentCaptor<String> sqlCaptor =
                ArgumentCaptor.forClass(String.class);

        verify(connection).prepareStatement(sqlCaptor.capture());

        String sql = sqlCaptor.getValue();

        assertTrue(sql.contains("WHERE country.Continent = ?"));
        assertTrue(sql.contains("ORDER BY city.Population DESC"));
        assertTrue(sql.contains("LIMIT ?"));

        verify(statement).setString(1, "Asia");
        verify(statement).setInt(2, 5);
    }
}