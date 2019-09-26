package de.jonas.spring.model;

public enum Metatype {
    HUMAN("Mensch",
            new Attributes(1, 1, 1, 1, 1, 1, 1, 1, 2, 6, 0),
            new Attributes(6, 6, 6, 6, 6, 6, 6, 6, 7, Integer.MAX_VALUE, Integer.MAX_VALUE)
    ),
    ELF("Elf",
            new Attributes(1, 2, 1, 1, 1, 1, 1, 3, 1, 6, 0),
            new Attributes(6, 7, 6, 6, 6, 6, 6, 8, 6, Integer.MAX_VALUE, Integer.MAX_VALUE)
    ),
    ORK("Ork",
            new Attributes(4, 1, 1, 3, 1, 1, 1, 1, 1, 6, 0),
            new Attributes(9, 6, 6, 8, 6, 5, 6, 5, 6, Integer.MAX_VALUE, Integer.MAX_VALUE)
    ),
    DWARF("Zwerg",
            new Attributes(3, 1, 1, 3, 2, 1, 1, 1, 1, 6, 0),
            new Attributes(8, 6, 5, 8, 7, 6, 6, 6, 6, Integer.MAX_VALUE, Integer.MAX_VALUE)
    ),
    TROLL("Troll",
            new Attributes(5, 1, 1, 5, 1, 1, 1, 1, 1, 6, 0),
            new Attributes(10, 5, 6, 10, 6, 5, 5, 4, 6, Integer.MAX_VALUE, Integer.MAX_VALUE)
    );

    private final String label;
    private final Attributes startingAttributes;
    private final Attributes attributeLimits;


    Metatype(String label, Attributes startingAttributes, Attributes attributeLimits) {
        this.label = label;
        this.startingAttributes = startingAttributes;
        this.attributeLimits = attributeLimits;
    }

    @Override
    public String toString() {
        return label;
    }

    public Attributes getStartingAttributes() {
        return startingAttributes;
    }

    public Attributes getAttributeLimits() {
        return attributeLimits;
    }
}
