
package com.zygon.htm.motor.sensor.impl;

import com.zygon.htm.core.io.input.image.Pixel;
import com.zygon.htm.motor.sensor.Reading;
import com.zygon.htm.motor.sensor.Sensor;

/**
 *
 * @author zygon
 */
public class ImageSensor implements Sensor<Pixel> {

    @Override
    public String getName() {
        return "ImageSensor TODO";
    }

    @Override
    public Reading<Pixel> getReading() {
        return null;
    }
}
