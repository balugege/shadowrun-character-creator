package de.jonas.spring.model;

public class Attributes {
    private int constitution;
    private int agility;
    private int reactivity;
    private int strength;
    private int willpower;
    private int logic;
    private int inteligence;
    private int charisma;
    private int edge;
    private int essence;

    public Attributes(int constitution, int agility, int reactivity, int strength, int willpower, int logic, int inteligence, int charisma, int edge, int essence) {
        this.constitution = constitution;
        this.agility = agility;
        this.reactivity = reactivity;
        this.strength = strength;
        this.willpower = willpower;
        this.logic = logic;
        this.inteligence = inteligence;
        this.charisma = charisma;
        this.edge = edge;
        this.essence = essence;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int getConstitution() {
        return constitution;
    }
}
