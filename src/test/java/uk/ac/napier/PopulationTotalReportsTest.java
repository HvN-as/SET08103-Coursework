
package uk.ac.napier;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PopulationTotalReportsTest {

    private long execute(String method, String argument)
            throws Exception {

        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet result = mock(ResultSet.class);

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);
        when(statement.executeQuery()).thenReturn(result);
        when(result.next()).thenReturn(true);
        when(result.getLong(1)).thenReturn(123456L);

        PopulationTotalReports reports =
                new PopulationTotalReports(connection);

        long population = switch (method) {
            case "world" -> reports.worldPopulation();
            case "continent" -> reports.continentPopulation(argument);
            case "region" -> reports.regionPopulation(argument);
            case "country" -> reports.countryPopulation(argument);
            case "district" -> reports.districtPopulation(argument);
            case "city" -> reports.cityPopulation(argument);
            default -> throw new IllegalArgumentException();
        };

        verify(statement).executeQuery();
        return population;
    }

    @Test
    void worldPopulation() throws Exception {
        assertEquals(123456L, execute("world", null));
    }

    @Test
    void continentPopulation() throws Exception {
        assertEquals(123456L, execute("continent", "Africa"));
    }

    @Test
    void regionPopulation() throws Exception {
        assertEquals(123456L, execute("region", "Eastern Africa"));
    }

    @Test
    void countryPopulation() throws Exception {
        assertEquals(123456L, execute("country", "Zimbabwe"));
    }

    @Test
    void districtPopulation() throws Exception {
        assertEquals(123456L, execute("district", "England"));
    }

    @Test
    void cityPopulation() throws Exception {
        assertEquals(123456L, execute("city", "London"));
    }

    @Test
    void rejectsBlankLocation() {
        Connection connection = mock(Connection.class);
        PopulationTotalReports reports =
                new PopulationTotalReports(connection);

        assertThrows(IllegalArgumentException.class,
                () -> reports.continentPopulation(" "));
    }

    @Test
    void rejectsNullConnection() {
        assertThrows(NullPointerException.class,
                () -> new PopulationTotalReports(null));
    }
}
