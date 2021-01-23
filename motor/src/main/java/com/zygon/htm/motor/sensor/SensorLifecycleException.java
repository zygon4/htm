/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.motor.sensor;

/**
 *
 * @author zygon
 */
public class SensorLifecycleException extends Exception {

    public SensorLifecycleException() {
    }

    public SensorLifecycleException(String string) {
        super(string);
    }

    public SensorLifecycleException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public SensorLifecycleException(Throwable thrwbl) {
        super(thrwbl);
    }
}
