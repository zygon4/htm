package com.zygon.htm.motor.sensor;

/**
 *
 * @author zygon
 */
public interface Sensor {

    String getName();

    String getUnits();

    State getState();

    Reading getReading() throws SensorReadingException;

    void doStart() throws SensorLifecycleException;

    void doStop() throws SensorLifecycleException;
}
