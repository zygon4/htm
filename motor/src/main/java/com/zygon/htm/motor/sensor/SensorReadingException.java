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
public class SensorReadingException extends Exception {

    public SensorReadingException() {
    }

    public SensorReadingException(String string) {
        super(string);
    }

    public SensorReadingException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public SensorReadingException(Throwable thrwbl) {
        super(thrwbl);
    }
}
