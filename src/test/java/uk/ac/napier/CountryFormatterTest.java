package uk.ac.napier;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CountryFormatterTest {

    @Test
    public void testFormatCountry() {
        Country country = new Country(
                "GBR", "United Kingdom", "Europe",
                "British Islands", 59623400L, "London"
        );

        String result = CountryFormatter.formatCountry(country);

        assertTrue(result.contains("GBR"));
        assertTrue(result.contains("United Kingdom"));
        assertTrue(result.contains("Europe"));
        assertTrue(result.contains("59623400"));
        assertTrue(result.contains("London"));
    }

    @Test
    public void testMissingCapital() {
        Country country = new Country(
                "TST", "Test Country", "Asia",
                "Test Region", 1000000L, null
        );

        String result = CountryFormatter.formatCountry(country);

        assertTrue(result.contains("N/A"));
    }

    @Test
    public void testFormatHeader() {
        String header = CountryFormatter.formatHeader();

        assertTrue(header.contains("Code"));
        assertTrue(header.contains("Name"));
        assertTrue(header.contains("Continent"));
        assertTrue(header.contains("Region"));
        assertTrue(header.contains("Population"));
        assertTrue(header.contains("Capital"));
    }
}
