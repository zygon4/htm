
package com.zygon.htm.motor.sensor;

/**
 * 
 * Really, just a wrapping for type - could stay or go.
 *
 * @author zygon
 * @param <T>
 */
public interface ReadingHandler<T extends Reading> {
    public void handle(String sensorName, T value);
}
