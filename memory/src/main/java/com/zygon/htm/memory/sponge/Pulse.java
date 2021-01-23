
package com.zygon.htm.memory.sponge;

import com.google.common.base.Preconditions;

/**
 *
 * @author zygon
 */
public class Pulse {

    private final Direction origin; // really should just knows relative origin point (ie direction)

    @Deprecated
    private final int strength; // This is really just TTL

    public static Pulse create (Direction origin) {
        return new Pulse(origin, 10);
    }

    private Pulse(Direction origin, int strength) {
        Preconditions.checkNotNull(origin);
        Preconditions.checkArgument(strength > 1);

        this.origin = origin;
        this.strength = strength;
    }

    public Pulse createPropagationPulse() {
        return new Pulse(this.getOrigin(), this.getStrength() - 1);
    }

    public Direction getOrigin() {
        return this.origin;
    }

    public int getStrength() {
        return this.strength;
    }

    public boolean hasMoreStrength() {
        return this.getStrength() > 1;
    }

    @Override
    public String toString() {
        return "P:{" + this.getOrigin() + ":" + this.getStrength() + "}";
    }
}
