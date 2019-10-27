package de.jonas.spring.model.skills;

import de.jonas.spring.model.AwokenType;
import de.jonas.spring.model.PlayerCharacter;

public enum PhysicalSkill implements Skill {
    ACROBATICS("Akrobatik"),
    RUNNING("Laufen"),
    SWIMMING("Schwimmen"),
    BIOTECHNOLOGY("Biotechnologie"),
    FIRST_AID("Erste Hilfe"),
    KYBERNETICS("Kybernetik"),
    MEDICINE("Medizin"),
    ELECTRONIC_WARFARE("Elektronische Kriegsführung"),
    HACKING("Hacking"),
    MATRIX_FIGHT("Matrixkampf"),
    LEADERSHIP("Führung"),
    HABITS("Gebräuche"),
    NEGOTIATION("Verhandlung"),
    COMPUTER("Computer"),
    HARDWARE("Hardware"),
    SOFTWARE("Software"),
    RIFLES("Gewehre"),
    PISTOLS("Pistolen"),
    MACHINE_GUNS("Schnellfeuerwaffen"),
    FINGER_ABILITY("Fingerfertigkeit"),
    SNEAK("Schleichen"),
    DRESS_UP("Verkleiden"),
    VEHICLE_MECHANICS("Fahrzeugmechanik"),
    INDUSTRY_MECHANICS("Industriemechanik"),
    AVIATION_MECHANICS("Luftfahrtmechanik"),
    NAUTICAL_MECHANICS("Seefahrtmechanik"),
    BLADE_WEAPONS("Klingenwaffen"),
    BLUNT("Knüppel"),
    WEAPONLESS_FIGHT("Waffenloser Kampf"),
    NAVIGATION("Navigation"),
    READ_TRACKS("Spurenlesen"),
    SURVIVAL("Survival"),
    CONVINCE("Überreden"),
    IMITATE("Verkörpern"),
    SHOWCASE("Vorführung"),
    DECOMPILE("Dekompilieren"),
    COMPILE("Kompilieren"),
    REGISTER("Registrieren");



    private final String label;

    PhysicalSkill(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
