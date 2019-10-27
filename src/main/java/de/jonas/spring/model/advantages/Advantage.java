package de.jonas.spring.model.advantages;

public enum Advantage {
    ANALYTIC_MIND("Analytischer Geist", 5, 1),
    ASTRAL_CHAMELEON("Astrales Chamäleon", 10, 1),
    OUTSTANDING_ATTRIBUTE("Aussergewöhnliches Attribut", 14, 1),
    TWOHANDEDNESS("Beidhändigkeit", 4, 1),
    MOVEMENT_TALENT("Bewegungstalent", 7, 1),
    ASSERTIVENESS("Durchsetzungskraft", 8, 3),
    IMPROVED_CONCENTRATION("Erhöhte Konzentrationsfähigkeit", 4, 5),
    PHOTOGRAPHIC_MEMORY("Fotografisches Gedächtnis", 6, 1),
    KIND_SPIRITS("Freundliche Geister", 7, 1),
    LUCK("Glück", 12, 1),
    ELASTIC_JOINTS("Gummigelenke", 6, 1),
    HIGH_PAIN_TOLERANCE("Hohe Schmerztoleranz", 7, 3),
    CAT_LIKE("Katzenhaft", 7, 1),
    MAGIC_RESISTANCE("Magieresistenz", 6, 4),
    HUMAN_APPEARANCE("Menschliches Aussehen", 6, 1),
    BRAVE("Mut", 10, 1),
    NATURAL_HARDNESS("Natürliche Härte", 10, 1),
    NATURAL_IMMUNITY_NATURAL("Natürliche Immunität (natürliches Toxin)", 4, 1),
    NATURAL_IMMUNITY_SYNTHETIC("Natürliche Immunität (synthetisches Toxin)", 10, 1),
    PATHOGEN_RESISTANCE("Pathogenresistenz", 4, 1),
    TOXIN_RESISTANCE("Toxinresistenz", 4, 1),
    PROGRAMMING_GENIUS("Programmiergenie", 10, 1),
    RACE_PILOT("Rennpilot", 11, 1),
    QUICK_HEAL("Schnellheilung", 3, 1),
    PROTECTIVE_SPIRIT("Schutzgeist", 3, 1), // Darf nur von magiern gelernt werden
    SOCIAL_CHAMELEON("Soziales Chamäleon", 11, 1),
    TALENTED("Talentiert", 14, 1),
    TECHNICAL_IMPROVISATION_TALENT("Technisches Improvisationstalent", 10, 1),
    WILL_TO_SURVIVE("Überlebenswille", 3, 3),
    SNEAK("Unauffälligkeit", 8, 1),
    TRUSTED_TERRAIN("Unauffälligkeit", 10, 1),
    TOUGHNESS("Zähigkeit", 9, 1),
    BILINGUAL("Zweisprachig", 5, 1),
    ALLERGY("Allergie", -5, 5),
    ASTRAL_LIGHT_FIRE("Astrales Leuchtfeuer", -10, 1),
    SHOWY("Auffälliger Stil", -5, 1),
    NOTORIOUS("Berüchtigt", -7, 1),
    HONOR_CODEX("Ehrenkodex", -15, 1),
    ELB_POSER("Elfenposer", -6, 1),
    HOSTILE_SPIRITS("Feindliche Geister", -7, 1),
    MARKED("Gezeichnet", -10, 1),
    GREMLINS("Gremlins", -4, 4),
    SHAKING_HANDS("Händezittern", -7, 1),
    IMMUNITY_REPELLENT("Immunabstosung", -12, 1),
    INCOMPETENCE("Inkompetenz", -5, 1),
    BATTLE_STUNNED("Kampflähmung", -12, 1),
    LOW_PAIN_TOLERANCE("Niedrige Schmerztoleranz", -9, 1),
    ORK_POSER("Orkposer", -6, 1),
    PROGRAMMING_NOOB("Programmierniete", -10, 1),
    SLEEPLESSNESS("Schlaflosigkeit (schwach)", -10, 1),
    STRONG_SLEEPLESSNESS("Schlaflosigkeit (stark)", -15, 1),
    WEAK_IMMUNITY_SYSTEM("Swaches Immunsystem", -10, 1),
    SIMSINN_DISORIENTATION("Swaches Immunsystem", -5, 1),
    SIN_HUMAN("SIN-Mensch", -5, 5),
    SOCIAL_STRESS("Sozialstress", -8, 1),
    UNEDUCATED("Ungebildet", -8, 1),
    RUDE("Ungehobelt", -14, 1),
    BAD_LUCK("Unglück", -12, 1),
    OBLIGATIONS("Verpflichtungen", -3, 3),
    UNCERTAIN("Verunsichert", -10, 1),
    PREJUDICE("Vorurteile", -3, 3);


    private final String name;
    private final int karmaCosts;

    Advantage(String name, int karmaCosts, int maxLevel) {
        this.name = name;
        this.karmaCosts = karmaCosts;
    }

    public String getName() {
        return name;
    }

    public int getKarmaCosts() {
        return karmaCosts;
    }

    public boolean isDisAdvantage() {
        return karmaCosts < 0;
    }
}

