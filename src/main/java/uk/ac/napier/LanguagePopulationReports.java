
package uk.ac.napier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.PrintStream;
import java.util.Objects;

public class LanguagePopulationReports {

    private final Connection connection;

    public LanguagePopulationReports(Connection connection) {
        this.connection = Objects.requireNonNull(connection);
    }

    public void showLanguageReport(PrintStream output)
            throws SQLException {

        Objects.requireNonNull(output);

        String sql = """
            SELECT cl.Language,
                   ROUND(SUM(c.Population * cl.Percentage / 100.0))
                       AS Speakers,
                   ROUND(
                       SUM(c.Population * cl.Percentage / 100.0)
                       / (SELECT SUM(Population) FROM country) * 100,
                       2
                   ) AS WorldPercentage
            FROM countrylanguage cl
            JOIN country c ON cl.CountryCode = c.Code
            WHERE cl.Language IN
                  ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic')
            GROUP BY cl.Language
            ORDER BY Speakers DESC
            """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            output.println("\n--- Major Language Population Report ---");
            output.printf("%-15s %-18s %-15s%n",
                    "Language", "Speakers", "World %");

            while (results.next()) {
                output.printf("%-15s %-18d %-15.2f%n",
                        results.getString("Language"),
                        results.getLong("Speakers"),
                        results.getDouble("WorldPercentage"));
            }
        }
    }
}
