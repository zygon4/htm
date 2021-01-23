
package com.zygon.htm.motor.actuator;

import java.util.Set;

/**
 *
 * @author zygon
 */
public interface Actuator {
    String getName();
    Set<String> getMovementNames();
    void activate(String movement);
}
