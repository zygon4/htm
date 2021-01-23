package com.zygon.htm.motor.sensor.camera;

import com.zygon.htm.motor.sensor.Reading;

/**
 * This shouldn't be a subclass.
 *
 * @author zygon
 */
@Deprecated
public class CameraReading extends Reading {

    public CameraReading(byte[][] value, long timestamp) {
        super(translateValue(value), timestamp);
    }

    private static byte[] translateValue(byte[][] value) {

        byte[] data = new byte[value.length * value[0].length];
        int dataIdx = 0;

        for (int i = 0; i < value[0].length; i++) {
            for (int j = 0; j < value.length; j++) {
                data[dataIdx++] = value[i][j];
            }
        }

        return data;
    }
}
