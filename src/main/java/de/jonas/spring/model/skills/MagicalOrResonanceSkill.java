package de.jonas.spring.model.skills;

public enum MagicalOrResonanceSkill {
    ALCHEMY("Alchemie", false),
    ANTIMAGIC("Antimagie", false),
    BINDING("Binden", false),
    DISENCHATING("Entzaubern", false),
    CRATE_FOCUS("Fokusherstellung", false),
    SUMMON("Herbeirufen", false),
    RITUAL("Ritaulzauberei", false),
    DEMANDING("Sprichzauberei", false),
    BANNING("Verbannen", false),
    DECOMPILING("Dekompilieren", true),
    COMPILING("Kompilieren", true),
    REGISTERING("Registrieren", true);

    private final String label;
    private boolean resonanceSkill;

    MagicalOrResonanceSkill(String label, boolean resonanceSkill) {
        this.label = label;
        this.resonanceSkill = resonanceSkill;
    }

    public boolean isResonanceSkill() {
        return resonanceSkill;
    }

    @Override
    public String toString() {
        return label;
    }
}
