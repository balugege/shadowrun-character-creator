package de.jonas.spring.model;

public class Attributes {
    private int constitution;
    private int agility;
    private int reactivity;
    private int strength;
    private int willpower;
    private int logic;
    private int intuition;
    private int charisma;
    private int edge;
    private int essence;
    private int magicOrResonance;

    Attributes(int constitution, int agility, int reactivity, int strength, int willpower, int logic, int intuition, int charisma, int edge, int essence, int magicOrResonance) {
        this.constitution = constitution;
        this.agility = agility;
        this.reactivity = reactivity;
        this.strength = strength;
        this.willpower = willpower;
        this.logic = logic;
        this.intuition = intuition;
        this.charisma = charisma;
        this.edge = edge;
        this.essence = essence;
        this.magicOrResonance = magicOrResonance;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getReactivity() {
        return reactivity;
    }

    public void setReactivity(int reactivity) {
        this.reactivity = reactivity;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getWillpower() {
        return willpower;
    }

    public void setWillpower(int willpower) {
        this.willpower = willpower;
    }

    public int getLogic() {
        return logic;
    }

    public void setLogic(int logic) {
        this.logic = logic;
    }

    public int getIntuition() {
        return intuition;
    }

    public void setIntuition(int intuition) {
        this.intuition = intuition;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getEdge() {
        return edge;
    }

    public int getMagicOrResonance() {
        return magicOrResonance;
    }

    public void setMagicOrResonance(int magicOrResonance) {
        this.magicOrResonance = magicOrResonance;
    }

    public void setEdge(int edge) {
        this.edge = edge;
    }

    public int getConstitution() {
        return constitution;
    }

    public int getSumOfNonSpecialAttributes() {
        return this.constitution +
                this.agility +
                this.reactivity +
                this.strength +
                this.willpower +
                this.logic +
                this.intuition +
                this.charisma;
    }

    public int getSumOfSpecialAttributes() {
        return this.magicOrResonance +
                this.edge;
    }

    public boolean anyNonSpecialAttributeAtOrAbove(Attributes attributeLimits) {
        return (this.constitution >= attributeLimits.constitution) ||
                (this.agility >= attributeLimits.constitution) ||
                (this.reactivity >= attributeLimits.constitution) ||
                (this.strength >= attributeLimits.constitution) ||
                (this.willpower >= attributeLimits.constitution) ||
                (this.logic >= attributeLimits.constitution) ||
                (this.intuition >= attributeLimits.constitution) ||
                (this.charisma >= attributeLimits.constitution);
    }

    public Attributes getSumWith(Attributes addend) {
        return new Attributes(
                this.constitution + addend.constitution,
                this.agility + addend.agility,
                this.reactivity + addend.reactivity,
                this.strength + addend.strength,
                this.willpower + addend.willpower,
                this.logic + addend.logic,
                this.intuition + addend.intuition,
                this.charisma + addend.charisma,
                this.edge + addend.edge,
                this.essence + addend.essence,
                this.magicOrResonance + addend.magicOrResonance
        );
    }
}
