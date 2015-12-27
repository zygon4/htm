
package com.zygon.htm.motor.sensor;

/**
 *
 * @author zygon
 * @param <T>
 */
public interface Sensor<T> {
    String getName();
    Reading<T> getReading();
}
