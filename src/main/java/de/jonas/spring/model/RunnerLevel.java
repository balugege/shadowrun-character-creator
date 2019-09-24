package de.jonas.spring.model;

public enum RunnerLevel {
    STREET("Strassenrunner", "Bla 1"),
    EXPERIENCED("Erfahrener Runner", "Bla 2"),
    TOP("Top-Runner", "Bla 3");

    private final String label;
    private final String description;

    RunnerLevel(String label, String description) {
        this.label = label;
        this.description = description;
    }

    @Override
    public String toString() {
        return label;
    }

    public String getDescription() {
        return this.description;
    }
}
