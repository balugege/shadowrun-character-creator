package de.jonas.spring.model;

public final class MetaTypeWithSpecialAttributes {

    private final Metatype metatype;
    private final int specialAttributePoints;

    MetaTypeWithSpecialAttributes(Metatype metatype, int specialAttributePoints) {
        this.metatype = metatype;
        this.specialAttributePoints = specialAttributePoints;
    }

    public Metatype getMetatype() {
        return metatype;
    }

    public int getSpecialAttributePoints() {
        return specialAttributePoints;
    }

    @Override
    public String toString() {
        return getMetatype().toString() + " (" + getSpecialAttributePoints() + ")";
    }
}
