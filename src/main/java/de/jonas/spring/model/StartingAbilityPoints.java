package de.jonas.spring.model;

public class StartingAbilityPoints {
    private final int singleAbilities;
    private final int abilityGroups;

    public StartingAbilityPoints(int singleAbilities, int abilityGroups) {
        this.singleAbilities = singleAbilities;
        this.abilityGroups = abilityGroups;
    }

    public int getSingleAbilities() {
        return singleAbilities;
    }

    public int getAbilityGroups() {
        return abilityGroups;
    }

    @Override
    public String toString() {
        return "Einzelfertigkeiten " + getSingleAbilities() + "<br>Gruppen " + getAbilityGroups();
    }
}
