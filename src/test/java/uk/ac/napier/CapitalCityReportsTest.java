
package uk.ac.napier;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CapitalCityReportsTest {

    @Test
    void rejectsZeroTopN() {
        Connection connection = mock(Connection.class);
        CapitalCityReports reports =
                new CapitalCityReports(connection);

        assertThrows(IllegalArgumentException.class,
                () -> reports.topCapitalsWorldwide(0));
    }

    @Test
    void rejectsNegativeTopN() {
        Connection connection = mock(Connection.class);
        CapitalCityReports reports =
                new CapitalCityReports(connection);

        assertThrows(IllegalArgumentException.class,
                () -> reports.topCapitalsWorldwide(-5));
    }

    @Test
    void rejectsBlankContinent() {
        Connection connection = mock(Connection.class);
        CapitalCityReports reports =
                new CapitalCityReports(connection);

        assertThrows(IllegalArgumentException.class,
                () -> reports.capitalsByContinent(""));
    }

    @Test
    void rejectsBlankRegion() {
        Connection connection = mock(Connection.class);
        CapitalCityReports reports =
                new CapitalCityReports(connection);

        assertThrows(IllegalArgumentException.class,
                () -> reports.capitalsByRegion(" "));
    }
}
