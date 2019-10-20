package de.jonas.spring.model.skills;

import de.jonas.spring.model.AwokenType;
import de.jonas.spring.model.PlayerCharacter;

public enum PhysicalSkill implements Skill {
    ACROBATICS("Akrobatik"),
    RUNNING("Laufen"),
    SWIMMING("Schwimmen"),
    BIOTECHNOLOGY("Biotechnologie"),
    FIRST_AID("Erste Hilfe"),
    KYBERNETICS("Kybernetik"),
    MEDICINE("Medizin");

    private final String label;

    PhysicalSkill(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
