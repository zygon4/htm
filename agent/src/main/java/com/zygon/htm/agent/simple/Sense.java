package com.zygon.htm.agent.simple;

import org.apache.commons.math3.util.Pair;

/**
 *
 * @author zygon
 */
public final class Sense {

    // This is sort of backwards, you should sense possibly unknown percepts
    public static enum Percept {
        ECHO,
        DRAFT,
        SMELL
    }

    private final Percept percept;
    private final Pair<Direction, Double> strengthByDirection;

    public Sense(Percept percept, Pair<Direction, Double> strengthByDirection) {
        this.percept = percept;
        this.strengthByDirection = strengthByDirection;
    }

    public final String getDisplayString() {
        return new StringBuilder()
                .append(getName())
                .append("-")
                .append(getStrengthByDirection())
                .toString();
    }

    public String getName() {
        return percept.name();
    }

    public Percept getPercept() {
        return percept;
    }

    public Pair<Direction, Double> getStrengthByDirection() {
        return strengthByDirection;
    }

    @Override
    public String toString() {
        return getDisplayString();
    }
}
