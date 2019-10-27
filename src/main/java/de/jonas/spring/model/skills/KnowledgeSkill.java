package de.jonas.spring.model.skills;

public class KnowledgeSkill implements Skill {
    private final String name;

    public KnowledgeSkill(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
