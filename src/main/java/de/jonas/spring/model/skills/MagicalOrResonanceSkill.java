package de.jonas.spring.model.skills;

import de.jonas.spring.model.AwokenType;
import de.jonas.spring.model.PlayerCharacter;

public enum MagicalOrResonanceSkill implements Skill {
    ALCHEMY("Alchemie", false),
    ANTIMAGIC("Antimagie", false),
    BINDING("Binden", false),
    DISENCHATING("Entzaubern", false),
    CRATE_FOCUS("Fokusherstellung", false),
    SUMMON("Herbeirufen", false),
    RITUAL("Ritaulzauberei", false),
    DEMANDING("Spruchzauberei", false),
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

    public boolean canPlayerLearn(PlayerCharacter player) {
        if (player.getAwokenType() == null) {
            return false;
        }
        return (player.getAwokenType() == AwokenType.TECHNOMANCER) == isResonanceSkill();
    }
}
