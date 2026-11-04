package com.napier.sem.models;

/**
 * Model representing language population estimates (Issue #6).
 */
public class LanguageReport {
    private String language;
    private long estimatedSpeakers;
    private double percentageOfWorldPopulation;

    public LanguageReport(String language, long estimatedSpeakers, double percentageOfWorldPopulation) {
        this.language = language;
        this.estimatedSpeakers = estimatedSpeakers;
        this.percentageOfWorldPopulation = percentageOfWorldPopulation;
    }

    public String getLanguage() { return language; }
    public long getEstimatedSpeakers() { return estimatedSpeakers; }
    public double getPercentageOfWorldPopulation() { return percentageOfWorldPopulation; }
}