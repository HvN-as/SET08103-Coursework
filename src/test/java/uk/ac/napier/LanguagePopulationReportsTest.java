
package uk.ac.napier;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LanguagePopulationReportsTest {

    @Test
    void reportContainsFiveLanguagesInPopulationOrder()
            throws Exception {

        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet results = mock(ResultSet.class);

        when(connection.prepareStatement(anyString()))
                .thenReturn(statement);
        when(statement.executeQuery()).thenReturn(results);

        when(results.next()).thenReturn(
                true, true, true, true, true, false);

        when(results.getString("Language")).thenReturn(
                "Chinese", "Hindi", "English", "Spanish", "Arabic");

        when(results.getLong("Speakers")).thenReturn(
                1000L, 800L, 600L, 400L, 200L);

        when(results.getDouble("WorldPercentage")).thenReturn(
                25.0, 20.0, 15.0, 10.0, 5.0);

        ByteArrayOutputStream buffer =
                new ByteArrayOutputStream();

        LanguagePopulationReports report =
                new LanguagePopulationReports(connection);

        report.showLanguageReport(new PrintStream(buffer));

        String output = buffer.toString();

        assertTrue(output.contains("Chinese"));
        assertTrue(output.contains("English"));
        assertTrue(output.contains("Hindi"));
        assertTrue(output.contains("Spanish"));
        assertTrue(output.contains("Arabic"));

        assertTrue(output.contains("1000"));
        assertTrue(output.contains("25.00"));

        assertTrue(output.indexOf("Chinese")
                < output.indexOf("Hindi"));
        assertTrue(output.indexOf("Hindi")
                < output.indexOf("English"));

        verify(statement).executeQuery();
    }

    @Test
    void rejectsNullConnection() {
        assertThrows(NullPointerException.class,
                () -> new LanguagePopulationReports(null));
    }

    @Test
    void rejectsNullOutput() {
        Connection connection = mock(Connection.class);
        LanguagePopulationReports report =
                new LanguagePopulationReports(connection);

        assertThrows(NullPointerException.class,
                () -> report.showLanguageReport(null));
    }
}
