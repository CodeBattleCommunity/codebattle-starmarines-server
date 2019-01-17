package com.epam.game.gamemodel.model;

/**
 * Represents type of a vertex with it's main parameters.
 * 
 * @author Evgeny_Tetuhin
 * 
 */
public enum VertexType {

    /**
     * Regeneration: +10% Maximum units: 100
     */
    TYPE_A(0.1, 100),

    /**
     * Regeneration: +15% Maximum units: 200
     */
    TYPE_B(0.15, 200),

    /**
     * Regeneration: +20% Maximum units: 500
     */
    TYPE_C(0.2, 500),
    /**
     * Regeneration: +30% Maximum units: 1000
     */
    TYPE_D(0.3, 1000);

    /**
     * Regeneration rate determines a fraction of units to be added to a
     * population in the vertex on every turn.
     */
    private double regen;
    /**
     * Regeneration stops after a population in the vertex grows up to that
     * value.
     */
    private int maxUnitsCount;

    private VertexType(double regen, int maxUnitsCount) {
        this.regen = regen;
        this.maxUnitsCount = maxUnitsCount;
    }

    /**
     * @return Regeneration rate
     */
    public double getRegenerationRate() {
        return this.regen;
    }

    /**
     * @return Maximum of units to be regenerated in the vertex.
     */
    public int getMaxUnits() {
        return this.maxUnitsCount;
    }
}
