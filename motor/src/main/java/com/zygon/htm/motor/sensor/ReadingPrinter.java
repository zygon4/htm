/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.motor.sensor;

import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author zygon
 */
public class ReadingPrinter implements ReadingHandler {

    @Override
    public void handle(String sensorName, Reading value) {
        System.out.println(new Date(value.getTimestamp()) + ") sensorName - "
                + Arrays.toString(value.getValue()));
    }
}
