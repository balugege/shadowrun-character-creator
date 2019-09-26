package de.jonas.spring.model;

import de.jonas.spring.model.skills.Skill;
import de.jonas.spring.model.skills.MagicalOrResonanceSkill;
import de.jonas.spring.model.skills.MagicalSkillGroup;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerCharacter {
    private Metatype metatype;
    private AwokenType awokenType;
    private String name;
    private RunnerLevel runnerLevel;
    private Map<Prioritizable, Priority> priorities = new EnumMap<>(Prioritizable.class);
    private Map<Skill, Integer> learnedSkills = new HashMap<>();
    private List<Castable> castables = new ArrayList<>();

    public List<Castable> getCastables() {
        return castables.stream().filter(castable -> castable.canPlayerLearn(this)).collect(Collectors.toList());
    }

    public List<Castable> getAllCastables() {
        return castables;
    }

    /**
     * @return Only skills that the player can cast.
     */
    public List<MagicalOrResonanceSkill> getMagicalOrResonanceSkills() {
        return (List) learnedSkills.keySet().stream().filter(skill -> skill instanceof MagicalOrResonanceSkill && ((MagicalOrResonanceSkill) skill).canPlayerLearn(this)).collect(Collectors.toList());
    }

    /**
     * Attributes bought at character creation
     */
    private Attributes boughtAttributes = new Attributes(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

    public MagicalSkillGroup getMagicalSkillGroup() {
        return (MagicalSkillGroup) learnedSkills.keySet().stream().filter(skill -> skill instanceof MagicalSkillGroup).findAny().orElse(null);
    }

    public void forgetSkill(Skill skill) {
        learnedSkills.remove(skill);
    }

    public void learnSkill(Skill skill, int level) {
        learnedSkills.put(skill, level);
    }

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
