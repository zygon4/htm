
package com.zygon.htm.memory.sponge;

import com.google.common.base.Preconditions;
import com.zygon.htm.core.Identifier;

/**
 * I guess? This probably doesn't need to be an adorned class.. but who knows.
 *
 * @author zygon
 */
public class Direction {

    private final Identifier origin;

    public Direction(Identifier origin) {
        this.origin = Preconditions.checkNotNull(origin);
    }

    public final Identifier getOrigin() {
        return this.origin;
    }

    public double getAbsoluteAngle(Identifier other) {
        return other.getDirection(this.origin);
//        return this.origin.getDirection(other);
    }

    @Override
    public String toString() {
        return this.origin.toString();
    }
}
