
package com.zygon.htm.motor.sensor;

import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author zygon
 * @param <T>
 */
public class SensorController<T extends Sensor<?>> {

    private final Collection<Sensor<T>> sensors;

    public SensorController(Collection<Sensor<T>> sensors) {
        this.sensors = Collections.unmodifiableCollection(sensors);
    }

    // aggregate?
}
