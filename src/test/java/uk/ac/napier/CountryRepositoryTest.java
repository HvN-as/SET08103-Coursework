
package uk.ac.napier;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CountryRepositoryTest {

    private final CountryRepository repository = new CountryRepository();

    @Test
    public void testTopCountriesWorldwideRejectsZero() {
        assertThrows(IllegalArgumentException.class, () ->
                repository.getTopCountriesWorldwide(null, 0));
    }

    @Test
    public void testTopCountriesWorldwideRejectsNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                repository.getTopCountriesWorldwide(null, -5));
    }

    @Test
    public void testTopCountriesByContinentRejectsZero() {
        assertThrows(IllegalArgumentException.class, () ->
                repository.getTopCountriesByContinent(null, "Europe", 0));
    }

    @Test
    public void testTopCountriesByRegionRejectsNegative() {
        assertThrows(IllegalArgumentException.class, () ->
                repository.getTopCountriesByRegion(null, "Eastern Africa", -10));
    }
}
