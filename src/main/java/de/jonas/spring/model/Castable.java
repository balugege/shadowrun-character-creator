package de.jonas.spring.model;

public enum Castable {
    EDITOR("Editor", CastableType.COMPLEX_FORM),
    MATRIXATRIBUT_DECREASE("[Matrixattribut]-Senkung", CastableType.COMPLEX_FORM),
    MATRIXATRIBUT_INCREASE("[Matrixattribut]-Steigerung", CastableType.COMPLEX_FORM),
    METAGRID("Metagitter", CastableType.COMPLEX_FORM),
    PETZE("Petze", CastableType.COMPLEX_FORM),
    PUPPETEER("Puppenspieler", CastableType.COMPLEX_FORM),
    CLEANER("Reiniger", CastableType.COMPLEX_FORM),
    RESONANCE_ILLUSION("Resonanzillusion", CastableType.COMPLEX_FORM),
    RESONANCE_CANAL("Resonanzkanal", CastableType.COMPLEX_FORM),
    RESONANCE_FOG("Resonanznebel", CastableType.COMPLEX_FORM),
    RESONANCE_SPIKE("Resonanzspike", CastableType.COMPLEX_FORM),
    SIGNAL_FOG("Signalschleier", CastableType.COMPLEX_FORM),
    SIGNAL_STORM("Signalsturm", CastableType.COMPLEX_FORM),
    FLICK_TOGETHER("Zusammenflicken", CastableType.COMPLEX_FORM),
    DECREASE_ATTRIBUTE("[Attribut] Senken", CastableType.SPELL),
    INCREASE_ATTRIBUTE("[Attribut] Steigern", CastableType.SPELL),
    DETOXIFICATION("Entgiftung", CastableType.SPELL),
    COUNTER_TOXIN("Gegenmittel", CastableType.SPELL),
    HEAL("Heilen", CastableType.SPELL),
    CURE("Krankheit heilen", CastableType.SPELL),
    PROPHYLAXIS("Prophylaxe", CastableType.SPELL),
    INCREASE_REFLEXES("Reflexe steigern", CastableType.SPELL),
    AIR_MASK("Sauerstoffmaske", CastableType.SPELL),
    PAIN_RESISTANCE("Schmerzresistenz", CastableType.SPELL),
    STABILISE("Stablilisieren", CastableType.SPELL),
    SILENT_STEP("Leiser schritt", CastableType.SPELL),
    MASK("Maske", CastableType.SPELL),
    PHYSICAL_MASK("Physische Maske", CastableType.SPELL),
    PAIN("Schmerz", CastableType.SPELL),
    MASS_PAIN("Massenschmerz", CastableType.SPELL),
    MUTE("Schweigen", CastableType.SPELL),
    SILENCE("Stille", CastableType.SPELL),
    ILLUSION("Trugbild", CastableType.SPELL),
    TRIDEO_ILLUSION("Trideo-Trugbild", CastableType.SPELL),
    INVISIBILITY("Unsichtbarkeit", CastableType.SPELL),
    IMPROVED_INVISIBILITY("Verbesserte Unsichtbarkeit", CastableType.SPELL),
    ENTERTAINMENT("Unterhaltung", CastableType.SPELL),
    TRIDEO_ENTERTAINMENT("Unterhaltung", CastableType.SPELL),
    CONFUSION("Verwirrung", CastableType.SPELL),
    MASS_CONFUSION("Massenverwirrung", CastableType.SPELL),
    CHAOS("Chaos", CastableType.SPELL),
    CHAOTIC_WORLD("Chaotische Welt", CastableType.SPELL),
    CRITTERS("Viecher", CastableType.SPELL),
    SWARM("Schwarm", CastableType.SPELL),
    SHOCK_HAND("Schockhand", CastableType.SPELL),
    NUMBNESS_FLASH("Betäubungsblitz", CastableType.SPELL),
    NUMBNESS_BALL("Betäubungsball", CastableType.SPELL),
    LIGHTING_RAY("Blitzstrahl", CastableType.SPELL),
    BALL_LIGHTNING("Kugelblitz", CastableType.SPELL),
    ENERGY_PUNCH("Energieschlag", CastableType.SPELL),
    ENERGY_FLASH("Energieblitz", CastableType.SPELL),
    ENERGY_BALL("Energieball", CastableType.SPELL),
    FLAME_THROWER("Flammenwerfer", CastableType.SPELL),
    FIRE_BALL("Feuerball", CastableType.SPELL),
    DEATH_HAND("Todeshand", CastableType.SPELL),
    MANA_FLASH("Manablitz", CastableType.SPELL),
    MANA_BALL("Manaball", CastableType.SPELL),
    ACID_RAY("Säurestrahl", CastableType.SPELL),
    ACID_WAVE("Säurewelle", CastableType.SPELL),
    PUNCH("Schlag", CastableType.SPELL),
    HIT("Stoss", CastableType.SPELL),
    PRESSURE_WAVE("Druckwelle", CastableType.SPELL),
    INFLUENCE("Beeinflussen", CastableType.SPELL),
    REVIVAL("Belebung", CastableType.SPELL),
    MASS_REVIVAL("Massenbelebung", CastableType.SPELL),
    ICE_COVER("Eisdecke", CastableType.SPELL),
    INFLAME("Entzünden", CastableType.SPELL),
    CONTROL_THOUGHT("Gedanken beherrschen", CastableType.SPELL),
    MOB_CONSCIOUSNESS("Mob-Bewusstsein", CastableType.SPELL),
    MASTER_ACTIONS("Handlungen beherrschen", CastableType.SPELL),
    MOB_CONTROL("Mob-Kontrolle", CastableType.SPELL),
    LEVITATE("Levitieren", CastableType.SPELL),
    LIGHT("Licht", CastableType.SPELL),
    MANA_BARRIER("Manabarriere", CastableType.SPELL),
    ARMOR("Panzerung", CastableType.SPELL),
    PHYSICAL_BARRIER("Physische barriere", CastableType.SPELL),
    POLTERGEIST("Poltergeist", CastableType.SPELL),
    SHADOW("Schatten", CastableType.SPELL),
    SLING("Schleuder", CastableType.SPELL),
    SPELL_FINGER("Zauberfinger", CastableType.SPELL),
    ;
    private final String label;
    private final CastableType type;

    Castable(String label, CastableType type) {
        this.label = label;
        this.type = type;
    }

    public CastableType getType() {
        return type;
    }

    @Override
    public String toString() {
        return label;
    }

    public boolean canPlayerLearn(PlayerCharacter playerCharacter) {
        AwokenType awokenType = playerCharacter.getAwokenType();
        if (awokenType == null) {
            return false;
        }
        return awokenType == AwokenType.TECHNOMANCER && type == CastableType.COMPLEX_FORM ||
                awokenType == AwokenType.ADEPT && type == CastableType.ADEPT_FORCE ||
                (awokenType == AwokenType.MAGE_ADEPT || awokenType == AwokenType.WIZARD || awokenType == AwokenType.ASPECT_WIZARD) && (type == CastableType.SPELL || type == CastableType.ALCHEMICAL || type == CastableType.RITUAL);
    }
}
