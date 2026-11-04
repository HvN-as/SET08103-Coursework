package com.napier.sem.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Model representing population breakdown for an area (Continent, Region, Country).
 * Issue #4
 */
public class PopulationBreakdown {
    private String areaName;
    private long totalPopulation;
    private long cityPopulation;
    private long outsideCityPopulation;
    private double cityPercentage;
    private double outsideCityPercentage;

    public PopulationBreakdown(String areaName, long totalPopulation, long cityPopulation) {
        this.areaName = areaName;
        this.totalPopulation = totalPopulation;
        this.cityPopulation = cityPopulation;

        // Outside-city population = total population - city population
        this.outsideCityPopulation = Math.max(0, totalPopulation - cityPopulation);

        // Calculate percentages safely (handle zero population)
        if (totalPopulation > 0) {
            this.cityPercentage = calculatePercentage(this.cityPopulation, totalPopulation);
            this.outsideCityPercentage = calculatePercentage(this.outsideCityPopulation, totalPopulation);
        } else {
            this.cityPercentage = 0.0;
            this.outsideCityPercentage = 0.0;
        }
    }

    private double calculatePercentage(long part, long total) {
        BigDecimal partBD = BigDecimal.valueOf(part);
        BigDecimal totalBD = BigDecimal.valueOf(total);
        return partBD.multiply(BigDecimal.valueOf(100))
                .divide(totalBD, 2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    // Getters
    public String getAreaName() { return areaName; }
    public long getTotalPopulation() { return totalPopulation; }
    public long getCityPopulation() { return cityPopulation; }
    public long getOutsideCityPopulation() { return outsideCityPopulation; }
    public double getCityPercentage() { return cityPercentage; }
    public double getOutsideCityPercentage() { return outsideCityPercentage; }
}