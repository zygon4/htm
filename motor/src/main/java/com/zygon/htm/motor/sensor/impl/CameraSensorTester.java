/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.motor.sensor.impl;

import com.zygon.htm.core.AbstractScheduledServiceImpl.Settings;
import com.zygon.htm.motor.sensor.NetworkClientSensor;
import com.zygon.htm.motor.sensor.ReadingPrinter;
import com.zygon.htm.motor.sensor.ScheduledSensor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class CameraSensorTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        NetworkClientSensor clientSensor = new NetworkClientSensor("192.168.1.7", 2222, 1280*720);
        ScheduledSensor scheduledNetworkSensor = new ScheduledSensor(
                // Trying to be 20 fps
                Settings.create(50, TimeUnit.MILLISECONDS), 
                clientSensor, 
                new ReadingPrinter());

        try {
            System.out.println("Starting the scheduled sensor");
            scheduledNetworkSensor.startAsync();
            Thread.sleep(TimeUnit.MINUTES.toMillis(2));
        } catch (InterruptedException i) {
            
        }
        System.out.println("Stopping the scheduled sensor");
        scheduledNetworkSensor.stopAsync();
    }
}
