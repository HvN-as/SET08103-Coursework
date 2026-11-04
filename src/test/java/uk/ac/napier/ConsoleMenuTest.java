
package uk.ac.napier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsoleMenuTest {

    private CapitalCityReports capitalReports;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setUp() {
        capitalReports = mock(CapitalCityReports.class);
        output = new ByteArrayOutputStream();
    }

    private String runMenu(String input) throws Exception {
        ByteArrayInputStream testInput =
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8));

        PrintStream testOutput =
                new PrintStream(output, true,
                        StandardCharsets.UTF_8);

        ConsoleMenu menu = new ConsoleMenu(
                capitalReports, testInput, testOutput);

        menu.run();

        return output.toString(StandardCharsets.UTF_8);
    }

    @Test
    void testExitOption() throws Exception {
        String result = runMenu("0\n");

        assertTrue(result.contains("Goodbye!"));
        verifyNoInteractions(capitalReports);
    }

    @Test
    void testInvalidMenuOption() throws Exception {
        String result = runMenu("99\n0\n");

        assertTrue(result.contains("Invalid menu option"));
        verifyNoInteractions(capitalReports);
    }

    @Test
    void testAllCapitalsWorldwide() throws Exception {
        runMenu("1\n0\n");

        verify(capitalReports).allCapitalsWorldwide();
    }

    @Test
    void testCapitalsByContinent() throws Exception {
        runMenu("2\nAfrica\n0\n");

        verify(capitalReports).capitalsByContinent("Africa");
    }

    @Test
    void testCapitalsByRegion() throws Exception {
        runMenu("3\nEastern Africa\n0\n");

        verify(capitalReports).capitalsByRegion("Eastern Africa");
    }

    @Test
    void testTopCapitalsWorldwide() throws Exception {
        runMenu("4\n10\n0\n");

        verify(capitalReports).topCapitalsWorldwide(10);
    }

    @Test
    void testTopCapitalsByContinent() throws Exception {
        runMenu("5\nEurope\n5\n0\n");

        verify(capitalReports)
                .topCapitalsByContinent("Europe", 5);
    }

    @Test
    void testTopCapitalsByRegion() throws Exception {
        runMenu("6\nWestern Europe\n3\n0\n");

        verify(capitalReports)
                .topCapitalsByRegion("Western Europe", 3);
    }

    @Test
    void testNegativeTopN() throws Exception {
        String result = runMenu("4\n-5\n0\n");

        assertTrue(result.contains(
                "Top N must be greater than zero"));

        verifyNoInteractions(capitalReports);
    }

    @Test
    void testZeroTopN() throws Exception {
        String result = runMenu("4\n0\n0\n");

        assertTrue(result.contains(
                "Top N must be greater than zero"));

        verifyNoInteractions(capitalReports);
    }

    @Test
    void testInvalidNumericInput() throws Exception {
        String result = runMenu("4\nabc\n0\n");

        assertTrue(result.contains(
                "Please enter a valid whole number"));

        verifyNoInteractions(capitalReports);
    }

    @Test
    void testBlankContinent() throws Exception {
        String result = runMenu("2\n   \n0\n");

        assertTrue(result.contains(
                "Value cannot be blank"));

        verifyNoInteractions(capitalReports);
    }

    @Test
    void testMultipleReports() throws Exception {
        runMenu("1\n4\n5\n0\n");

        verify(capitalReports).allCapitalsWorldwide();
        verify(capitalReports).topCapitalsWorldwide(5);
    }

    @Test
    void testEndOfInput() throws Exception {
        String result = runMenu("");

        assertTrue(result.contains(
                "Input closed. Exiting."));

        verifyNoInteractions(capitalReports);
    }
}
