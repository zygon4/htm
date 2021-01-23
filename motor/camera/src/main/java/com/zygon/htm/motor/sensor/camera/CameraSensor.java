/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.motor.sensor.camera;

import com.zygon.htm.motor.sensor.NetworkClientSensor;
import com.zygon.htm.motor.sensor.Reading;
import com.zygon.htm.motor.sensor.util.ImageUtils;

import java.util.Objects;

/**
 *
 * @author zygon
 */
public class CameraSensor extends NetworkClientSensor {

    private final Camera camera;
    private final String name;

    public CameraSensor(String remoteHost, int remotePort, int dataPacketLen, Camera camera, String name) {
        super(remoteHost, remotePort, dataPacketLen);
        this.camera = Objects.requireNonNull(camera);
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUnits() {
        return "pixel";
    }

    @Override
    public Reading getReading() {
        int[] stillAsRGB = camera.takeStillAsRGB(500, 500, true);

        for (int i = 0; i < stillAsRGB.length; i++) {
            int[] rgb = ImageUtils.getRGB(stillAsRGB[i]);

        }
        return null;
    }
}
