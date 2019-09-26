package de.jonas.spring.model;

import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Priority {
    A(new MetaTypeWithSpecialAttributes[]{
            new MetaTypeWithSpecialAttributes(Metatype.HUMAN, 9),
            new MetaTypeWithSpecialAttributes(Metatype.ELF, 8),
            new MetaTypeWithSpecialAttributes(Metatype.ORK, 7),
            new MetaTypeWithSpecialAttributes(Metatype.DWARF, 7),
            new MetaTypeWithSpecialAttributes(Metatype.TROLL, 5)
    }, 24, new MagicOrResonance[]{
            new MagicOrResonance(AwokenType.WIZARD, 6, 2, 0, 5, 10, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.MAGE_ADEPT, 6, 2, 0, 5, 10, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.TECHNOMANCER, 0, 0, 0, 0, 0, 6, 2, 5, 5, 0)
    }, new StartingAbilityPoints(46, 10), 450_000),
    B(new MetaTypeWithSpecialAttributes[]{
            new MetaTypeWithSpecialAttributes(Metatype.HUMAN, 7),
            new MetaTypeWithSpecialAttributes(Metatype.ELF, 6),
            new MetaTypeWithSpecialAttributes(Metatype.ORK, 4),
            new MetaTypeWithSpecialAttributes(Metatype.DWARF, 4),
            new MetaTypeWithSpecialAttributes(Metatype.TROLL, 0)
    }, 20, new MagicOrResonance[]{
            new MagicOrResonance(AwokenType.WIZARD, 4, 2, 0, 4, 7, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.MAGE_ADEPT, 4, 2, 0, 4, 7, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.TECHNOMANCER, 0, 0, 0, 0, 0, 4, 2, 4, 2, 0),
            new MagicOrResonance(AwokenType.ADEPT, 6, 0, 0, 0, 0, 0, 0, 0, 0, 4),
            new MagicOrResonance(AwokenType.ASPECT_WIZARD, 5, 0, 4, 0, 0, 0, 0, 0, 0, 0)
    }, new StartingAbilityPoints(36, 5), 275_000),
    C(new MetaTypeWithSpecialAttributes[]{
            new MetaTypeWithSpecialAttributes(Metatype.HUMAN, 5),
            new MetaTypeWithSpecialAttributes(Metatype.ELF, 3),
            new MetaTypeWithSpecialAttributes(Metatype.DWARF, 1),
            new MetaTypeWithSpecialAttributes(Metatype.ORK, 0),
    }, 16, new MagicOrResonance[]{
            new MagicOrResonance(AwokenType.WIZARD, 3, 0, 0, 0, 5, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.MAGE_ADEPT, 3, 0, 0, 0, 5, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.TECHNOMANCER, 0, 0, 0, 0, 0, 3, 0, 0, 1, 0),
            new MagicOrResonance(AwokenType.ADEPT, 4, 0, 0, 0, 0, 0, 0, 0, 0, 2),
            new MagicOrResonance(AwokenType.ASPECT_WIZARD, 5, 0, 4, 0, 0, 0, 0, 0, 0, 0)
    }, new StartingAbilityPoints(28, 2), 140_000),
    D(new MetaTypeWithSpecialAttributes[]{
            new MetaTypeWithSpecialAttributes(Metatype.HUMAN, 3),
            new MetaTypeWithSpecialAttributes(Metatype.ELF, 0),
    }, 14, new MagicOrResonance[]{
            new MagicOrResonance(AwokenType.ADEPT, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0),
            new MagicOrResonance(AwokenType.ASPECT_WIZARD, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    }, new StartingAbilityPoints(22, 0), 50_000),
    E(new MetaTypeWithSpecialAttributes[]{
            new MetaTypeWithSpecialAttributes(Metatype.HUMAN, 1),
    }, 12, new MagicOrResonance[0], new StartingAbilityPoints(18, 0), 6_000);

    private final MetaTypeWithSpecialAttributes[] metaTypes;
    private final int attributePoints;
    private final MagicOrResonance[] magicOrResonances;
    private final StartingAbilityPoints startingAbilityPoints;
    private final int resources;

    Priority(MetaTypeWithSpecialAttributes[] metaTypes, int attributePoints, MagicOrResonance[] magicOrResonances, StartingAbilityPoints startingAbilityPoints, int resources) {
        this.metaTypes = metaTypes;
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

    public MetaTypeWithSpecialAttributes[] getMetaTypes() {
        return metaTypes;
    }

    public MetaTypeWithSpecialAttributes getMetaType(Metatype metatype) {
        return Arrays.stream(metaTypes).filter(metaTypeWithSpecialAttributes -> metaTypeWithSpecialAttributes.getMetatype() == metatype).findAny().orElse(null);
    }

    public String getMetatypesDescription() {
        return Arrays.stream(metaTypes).map(MetaTypeWithSpecialAttributes::toString).collect(Collectors.joining("<br>"));
    }

    public MagicOrResonance[] getMagicOrResonances() {
        return magicOrResonances;
    }

    public MagicOrResonance getMagicOrResonance(AwokenType awokenType) {
        return Arrays.stream(magicOrResonances).filter(magicOrResonance -> magicOrResonance.getAwokenType() == awokenType).findAny().orElse(null);
    }

    public String getMagicOrResonanceDescription() {
        String collect = Arrays.stream(magicOrResonances).map(MagicOrResonance::toString).collect(Collectors.joining("<br>"));
        if (Strings.isEmpty(collect)) {
            return "Keine";
        } else {
            return collect;
        }
    }
}
