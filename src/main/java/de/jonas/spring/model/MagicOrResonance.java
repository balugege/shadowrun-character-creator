package de.jonas.spring.model;

import org.apache.logging.log4j.util.Strings;

public class MagicOrResonance {
    private final AwokenType awokenType;
    private final int magic;
    private final int magicalAbilities;
    private final int magicalAbilityGroupLevel;
    private final int magicalAbilityLevels;
    private final int spellsRitualsAlchemy;
    private final int resonance;
    private final int resonanceAbilities;
    private final int resonanceAbilityLevels;
    private final int complexForms;
    private final int actionAbilityLevel;

    public MagicOrResonance(AwokenType awokenType, int magic, int magicalAbilities, int magicalAbilityGroupLevel, int magicalAbilityLevels, int spellsRitualsAlchemy, int resonance, int resonanceAbilities, int resonanceAbilityLevels, int complexForms, int actionAbilityLevel) {
        this.awokenType = awokenType;
        this.magic = magic;
        this.magicalAbilities = magicalAbilities;
        this.magicalAbilityLevels = magicalAbilityLevels;
        this.magicalAbilityGroupLevel = magicalAbilityGroupLevel;
        this.spellsRitualsAlchemy = spellsRitualsAlchemy;
        this.resonance = resonance;
        this.resonanceAbilities = resonanceAbilities;
        this.resonanceAbilityLevels = resonanceAbilityLevels;
        this.complexForms = complexForms;
        this.actionAbilityLevel = actionAbilityLevel;
    }

    public AwokenType getAwokenType() {
        return awokenType;
    }

    @Override
    public String toString() {
        String description = "<b>" + awokenType.toString() + "</b>:";
        if (magic != 0) {
            description += " Magie " + magic + ", ";
        }

        if (magicalAbilities != 0) {
            description += magicalAbilities + " Fähigkeiten Level " + magicalAbilityLevels + ", ";
        }
        if (magicalAbilityGroupLevel != 0) {
            description += " Eine Fertigkeitengruppe Level " + magicalAbilityGroupLevel;
        }
        if (spellsRitualsAlchemy != 0) {
            description += spellsRitualsAlchemy + " Zauber";
        }
        if (resonance != 0) {
            description += " Resonanz " + resonance + ", ";
        }
        if (resonanceAbilities != 0) {
            description += resonanceAbilities + " Fertigkeiten Stufe " + resonanceAbilityLevels + ", ";
        }
        if (complexForms != 0) {
            description += complexForms + " Komplexe Formen";
        }
        if (actionAbilityLevel != 0) {
            description += " Eine Fertigkeit Stufe " + actionAbilityLevel;
        }

        if (Strings.isEmpty(description)) {
            description = "Keine";
        }

        return description;
    }
}
