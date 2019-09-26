package de.jonas.spring.model;

public enum Prioritizable {
    METATYPE("Metatyp (Spezialattributspunkte)"),
    ATTRIBUTES("Attributspunkte"),
    MAGIC_OR_RESONANZ("Magie oder Resonanz"),
    ABILITY_POINTS("Fertigkeiten"),
    RESOURCES("Ressourcen");

    private final String label;

    Prioritizable(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
