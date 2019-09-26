package de.jonas.spring.model.skills;

public enum MagicalSkillGroup implements Skill {
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
