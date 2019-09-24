package de.jonas.spring.model;

import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Priority {
    A(new MetatypeWithSpecialAttributes[]{
            new MetatypeWithSpecialAttributes(Metatype.HUMAN, 9),
            new MetatypeWithSpecialAttributes(Metatype.ELF, 8),
            new MetatypeWithSpecialAttributes(Metatype.ORK, 7),
            new MetatypeWithSpecialAttributes(Metatype.DWARF, 7),
            new MetatypeWithSpecialAttributes(Metatype.TROLL, 5)
    }, 24, new MagicOrResonance[]{
            new MagicOrResonance(AwokenType.WIZARD, 6, 2, 0, 5, 10, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.MAGE_ADEPT, 6, 2, 0, 5, 10, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.TECHNOMANCER, 0, 0, 0, 0, 0, 6, 2, 5, 5, 0)
    }, new StartingAbilityPoints(46, 10), 450_000),
    B(new MetatypeWithSpecialAttributes[]{
            new MetatypeWithSpecialAttributes(Metatype.HUMAN, 7),
            new MetatypeWithSpecialAttributes(Metatype.ELF, 6),
            new MetatypeWithSpecialAttributes(Metatype.ORK, 4),
            new MetatypeWithSpecialAttributes(Metatype.DWARF, 4),
            new MetatypeWithSpecialAttributes(Metatype.TROLL, 0)
    }, 20, new MagicOrResonance[]{
            new MagicOrResonance(AwokenType.WIZARD, 4, 2, 0, 4, 7, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.MAGE_ADEPT, 4, 2, 0, 4, 7, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.TECHNOMANCER, 0, 0, 0, 0, 0, 4, 2, 4, 2, 0),
            new MagicOrResonance(AwokenType.ADEPT, 6, 0, 0, 0, 0, 0, 0, 0, 0, 4),
            new MagicOrResonance(AwokenType.ASPECT_WIZARD, 5, 0, 4, 0, 0, 0, 0, 0, 0, 0)
    }, new StartingAbilityPoints(36, 5), 275_000),
    C(new MetatypeWithSpecialAttributes[]{
            new MetatypeWithSpecialAttributes(Metatype.HUMAN, 5),
            new MetatypeWithSpecialAttributes(Metatype.ELF, 3),
            new MetatypeWithSpecialAttributes(Metatype.DWARF, 1),
            new MetatypeWithSpecialAttributes(Metatype.ORK, 0),
    }, 16, new MagicOrResonance[]{
            new MagicOrResonance(AwokenType.WIZARD, 3, 0, 0, 0, 5, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.MAGE_ADEPT, 3, 0, 0, 0, 5, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.TECHNOMANCER, 0, 0, 0, 0, 0, 3, 0, 0, 1, 0),
            new MagicOrResonance(AwokenType.ADEPT, 4, 0, 0, 0, 0, 0, 0, 0, 0, 2),
            new MagicOrResonance(AwokenType.ASPECT_WIZARD, 5, 0, 4, 0, 0, 0, 0, 0, 0, 0)
    }, new StartingAbilityPoints(28, 2), 140_000),
    D(new MetatypeWithSpecialAttributes[]{
            new MetatypeWithSpecialAttributes(Metatype.HUMAN, 3),
            new MetatypeWithSpecialAttributes(Metatype.ELF, 0),
    }, 14, new MagicOrResonance[]{
            new MagicOrResonance(AwokenType.ADEPT, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.ASPECT_WIZARD, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    }, new StartingAbilityPoints(22, 0), 50_000),
    E(new MetatypeWithSpecialAttributes[]{
            new MetatypeWithSpecialAttributes(Metatype.HUMAN, 1),
    }, 12, new MagicOrResonance[0], new StartingAbilityPoints(18, 0), 6_000);

    private final MetatypeWithSpecialAttributes[] metatypes;
    private final int attributePoints;
    private final MagicOrResonance[] magicOrResonances;
    private final StartingAbilityPoints startingAbilityPoints;
    private final int resources;

    Priority(MetatypeWithSpecialAttributes[] metatypes, int attributePoints, MagicOrResonance[] magicOrResonances, StartingAbilityPoints startingAbilityPoints, int resources) {
        this.metatypes = metatypes;
        this.attributePoints = attributePoints;
        this.magicOrResonances = magicOrResonances;
        this.startingAbilityPoints = startingAbilityPoints;
        this.resources = resources;
    }

    public int getResources() {
        return resources;
    }

    public int getAttributePoints() {
        return attributePoints;
    }

    public StartingAbilityPoints getStartingAbilityPoints() {
        return startingAbilityPoints;
    }

    public MetatypeWithSpecialAttributes[] getMetatypes() {
        return metatypes;
    }

    public String getMetatypesDescription() {
        return Arrays.stream(metatypes).map(MetatypeWithSpecialAttributes::toString).collect(Collectors.joining("<br>"));
    }

    public MagicOrResonance[] getMagicOrResonances() {
        return magicOrResonances;
    }
    public String getMagicOrResonanceDescription() {
        String collect = Arrays.stream(magicOrResonances).map(MagicOrResonance::toString).collect(Collectors.joining("<br>"));
        if(Strings.isEmpty(collect)) {
            return "Keine";
        } else {
            return collect;
        }
    }
}
