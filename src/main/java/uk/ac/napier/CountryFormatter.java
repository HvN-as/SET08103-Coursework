package uk.ac.napier;

public class CountryFormatter {

    // Format one country's information
    public static String formatCountry(Country country) {
        return String.format("%-5s %-35s %-20s %-30s %-15d %-30s",
                country.getCode(),
                country.getName(),
                country.getContinent(),
                country.getRegion(),
                country.getPopulation(),
                country.getCapital() == null ? "N/A" : country.getCapital());
    }

    // Format the report column headings
    public static String formatHeader() {
        return String.format("%-5s %-35s %-20s %-30s %-15s %-30s",
                "Code",
                "Name",
                "Continent",
                "Region",
                "Population",
                "Capital");
    }
}
