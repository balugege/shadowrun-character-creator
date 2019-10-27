package de.jonas.spring.model.skills;

import java.util.List;

import static de.jonas.spring.model.skills.PhysicalSkill.*;
import static java.util.Arrays.asList;

public enum PhysicalSkillGroup implements SkillGroup {
    ATHLETICS("Athletik", asList(ACROBATICS, RUNNING, SWIMMING)),
    BIOTECH("Biotech", asList(BIOTECHNOLOGY, FIRST_AID, KYBERNETICS, MEDICINE));

    private final String label;

    PhysicalSkillGroup(String label, List<PhysicalSkill> containedSkills) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label + " (Gruppe)";
    }
}
