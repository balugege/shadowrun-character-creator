package de.jonas.spring.model;

public enum MagicalSkillGroup {
    WITCHCRAFT("Hexerei"),
    SUMMONING("Beschwören"),
    SORCERY("Verzaubern");

    private String label;

    MagicalSkillGroup(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
