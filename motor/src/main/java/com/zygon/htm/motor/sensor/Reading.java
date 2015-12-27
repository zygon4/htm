
package com.zygon.htm.motor.sensor;

/**
 *
 * @author zygon
 * @param <T>
 */
public final class Reading<T> {

    private final T value;
    private final long timestamp;

    public Reading(T value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    T getValue() {
        return value;
    }
}
