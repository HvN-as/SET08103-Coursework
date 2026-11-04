package com.napier.sem.services;

import com.napier.sem.models.PopulationBreakdown;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service handling population breakdowns (Issue #4) and lookups (Issue #5).
 */
public class PopulationReportService {
    private Connection con;

    public PopulationReportService(Connection con) {
        this.con = con;
    }

    /**
     * Issue #4: Population breakdown by Continent
     */
    public List<PopulationBreakdown> getPopulationBreakdownByContinent() {
        List<PopulationBreakdown> reports = new ArrayList<>();
        String query = "SELECT " +
                "c.Continent AS AreaName, " +
                "SUM(c.Population) AS TotalPopulation, " +
                "COALESCE(SUM(ci.CityPop), 0) AS CityPopulation " +
                "FROM country c " +
                "LEFT JOIN (SELECT CountryCode, SUM(Population) AS CityPop FROM city GROUP BY CountryCode) ci " +
                "ON c.Code = ci.CountryCode " +
                "GROUP BY c.Continent " +
                "ORDER BY TotalPopulation DESC;";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reports.add(new PopulationBreakdown(
                        rs.getString("AreaName"),
                        rs.getLong("TotalPopulation"),
                        rs.getLong("CityPopulation")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching continent population breakdown: " + e.getMessage());
        }
        return reports;
    }

    /**
     * Issue #4: Population breakdown by Region
     */
    public List<PopulationBreakdown> getPopulationBreakdownByRegion() {
        List<PopulationBreakdown> reports = new ArrayList<>();
        String query = "SELECT " +
                "c.Region AS AreaName, " +
                "SUM(c.Population) AS TotalPopulation, " +
                "COALESCE(SUM(ci.CityPop), 0) AS CityPopulation " +
                "FROM country c " +
                "LEFT JOIN (SELECT CountryCode, SUM(Population) AS CityPop FROM city GROUP BY CountryCode) ci " +
                "ON c.Code = ci.CountryCode " +
                "GROUP BY c.Region " +
                "ORDER BY TotalPopulation DESC;";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reports.add(new PopulationBreakdown(
                        rs.getString("AreaName"),
                        rs.getLong("TotalPopulation"),
                        rs.getLong("CityPopulation")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching region population breakdown: " + e.getMessage());
        }
        return reports;
    }

    /**
     * Issue #4: Population breakdown by Country
     */
    public List<PopulationBreakdown> getPopulationBreakdownByCountry() {
        List<PopulationBreakdown> reports = new ArrayList<>();
        String query = "SELECT " +
                "c.Name AS AreaName, " +
                "c.Population AS TotalPopulation, " +
                "COALESCE(SUM(ci.Population), 0) AS CityPopulation " +
                "FROM country c " +
                "LEFT JOIN city ci ON c.Code = ci.CountryCode " +
                "GROUP BY c.Code, c.Name, c.Population " +
                "ORDER BY TotalPopulation DESC;";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reports.add(new PopulationBreakdown(
                        rs.getString("AreaName"),
                        rs.getLong("TotalPopulation"),
                        rs.getLong("CityPopulation")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching country population breakdown: " + e.getMessage());
        }
        return reports;
    }
}

/**
 * Issue #5: Get population of the World
 */
public long getPopulationOfWorld() {
    String query = "SELECT SUM(Population) AS TotalPop FROM country;";
    try (Statement stmt = con.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {
        if (rs.next()) {
            return rs.getLong("TotalPop");
        }
    } catch (SQLException e) {
        System.out.println("Error fetching world population: " + e.getMessage());
    }
    return 0;
}

/**
 * Issue #5: Get population of a Continent
 */
public long getPopulationOfContinent(String continentName) {
    String query = "SELECT SUM(Population) AS TotalPop FROM country WHERE Continent = ?;";
    try (PreparedStatement pstmt = con.prepareStatement(query)) {
        pstmt.setString(1, continentName);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("TotalPop");
            }
        }
    } catch (SQLException e) {
        System.out.println("Error fetching continent population: " + e.getMessage());
    }
    return 0;
}

/**
 * Issue #5: Get population of a Region
 */
public long getPopulationOfRegion(String regionName) {
    String query = "SELECT SUM(Population) AS TotalPop FROM country WHERE Region = ?;";
    try (PreparedStatement pstmt = con.prepareStatement(query)) {
        pstmt.setString(1, regionName);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("TotalPop");
            }
        }
    } catch (SQLException e) {
        System.out.println("Error fetching region population: " + e.getMessage());
    }
    return 0;
}

/**
 * Issue #5: Get population of a Country
 */
public long getPopulationOfCountry(String countryName) {
    String query = "SELECT Population FROM country WHERE Name = ?;";
    try (PreparedStatement pstmt = con.prepareStatement(query)) {
        pstmt.setString(1, countryName);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("Population");
            }
        }
    } catch (SQLException e) {
        System.out.println("Error fetching country population: " + e.getMessage());
    }
    return 0;
}

/**
 * Issue #5: Get population of a District (sum of matching city populations)
 */
public long getPopulationOfDistrict(String districtName) {
    String query = "SELECT SUM(Population) AS TotalPop FROM city WHERE District = ?;";
    try (PreparedStatement pstmt = con.prepareStatement(query)) {
        pstmt.setString(1, districtName);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("TotalPop");
            }
        }
    } catch (SQLException e) {
        System.out.println("Error fetching district population: " + e.getMessage());
    }
    return 0;
}

/**
 * Issue #5: Get population of a City (handling name ambiguity with country code qualifier)
 */
public long getPopulationOfCity(String cityName, String countryCode) {
    String query = "SELECT Population FROM city WHERE Name = ? AND CountryCode = ?;";
    try (PreparedStatement pstmt = con.prepareStatement(query)) {
        pstmt.setString(1, cityName);
        pstmt.setString(2, countryCode);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getLong("Population");
            }
        }
    } catch (SQLException e) {
        System.out.println("Error fetching city population: " + e.getMessage());
    }
    return 0;
}