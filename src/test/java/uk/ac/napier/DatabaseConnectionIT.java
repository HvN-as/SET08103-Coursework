
package uk.ac.napier;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionIT {

    @Test
    void shouldConnectToWorldDatabase() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {

            assertNotNull(connection);
            assertFalse(connection.isClosed());

            try (Statement statement = connection.createStatement();
                 ResultSet result = statement.executeQuery(
                         "SELECT COUNT(*) FROM country")) {

                assertTrue(result.next());
                assertEquals(239, result.getInt(1));
            }
        }
    }
}
