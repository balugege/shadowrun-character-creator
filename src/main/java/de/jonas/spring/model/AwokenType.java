package de.jonas.spring.model;

public enum AwokenType {
    WIZARD("Zauberer"),
    MAGE_ADEPT("Magieradept"),
    ADEPT("Adept"),
    TECHNOMANCER("Technomanzer"),
    ASPECT_WIZARD("Aspektzauberer");

    private final String label;

    AwokenType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
