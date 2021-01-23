
package com.zygon.htm.motor.sensor;

import java.util.Objects;

/**
 *
 * @author zygon
 */
public class Reading {

    private final byte[] value;
    private final long timestamp;

    public Reading(byte[] value, long timestamp) {
        this.value = Objects.requireNonNull(value);
        this.timestamp = timestamp;
    }

    public final long getTimestamp() {
        return timestamp;
    }

    public final byte[] getValue() {
        return value;
    }
}
