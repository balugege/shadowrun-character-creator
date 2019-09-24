package de.jonas.spring.model;

import java.util.EnumMap;
import java.util.Map;

public class PlayerCharacter {
    private Metatype metatype;
    private AwokenType awokenType;
    private String name;
    private RunnerLevel runnerLevel;
    private Map<Prioritizable, Priority> priorities = new EnumMap<>(Prioritizable.class);
    /**
     * Attributes bought at character creation
     */
    private Attributes boughtAttributes = new Attributes(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    public MagicalSkillGroup getMagicalSkillGroup() {
        return magicalSkillGroup;
    }

    public void setMagicalSkillGroup(MagicalSkillGroup magicalSkillGroup) {
        this.magicalSkillGroup = magicalSkillGroup;
    }

    private MagicalSkillGroup magicalSkillGroup;

    public AwokenType getAwokenType() {
        return awokenType;
    }

    public void setAwokenType(AwokenType awokenType) {
        this.awokenType = awokenType;
    }

    public void setMetatype(Metatype metatype) {
        this.metatype = metatype;
    }

    public Metatype getMetatype() {
        return metatype;
    }

    public PlayerCharacter() {
    }

    public Attributes getBoughtAttributes() {
        return boughtAttributes;
    }

    public void setPriority(Prioritizable prioritizable, Priority priority) {
        priorities.put(prioritizable, priority);
    }

    public Priority getPriority(Prioritizable prioritizable) {
        return priorities.get(prioritizable);
    }

    public RunnerLevel getRunnerLevel() {
        return runnerLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRunnerLevel(RunnerLevel runnerLevel) {
        this.runnerLevel = runnerLevel;
    }
}
