package de.jonas.spring.model;

public class Attributes {
    private int constitution;
    private int agility;
    private int reactivity;
    private int strength;
    private int willpower;
    private int logic;
    private int intelligence;
    private int charisma;
    private int edge;
    private int essence;

    public Attributes(int constitution, int agility, int reactivity, int strength, int willpower, int logic, int intelligence, int charisma, int edge, int essence) {
        this.constitution = constitution;
        this.agility = agility;
        this.reactivity = reactivity;
        this.strength = strength;
        this.willpower = willpower;
        this.logic = logic;
        this.intelligence = intelligence;
        this.charisma = charisma;
        this.edge = edge;
        this.essence = essence;
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

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
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

    public void setEdge(int edge) {
        this.edge = edge;
    }

    public int getConstitution() {
        return constitution;
    }

    public int getSumOfBuyable() {
        return this.constitution + this.agility + this.reactivity + this.strength + this.willpower + this.logic + this.intelligence + this.charisma + this.edge;
    }
}
