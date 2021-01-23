/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.motor.sensor.camera;

import com.google.common.base.Preconditions;
import java.util.Objects;

/**
 * Maybe should be video stream??
 * "/home/pi/Pictures/linkfile.png"
 *
 * @author zygon
 */
public class Camera {
    private final String fileToRead;
    private final long imageStillDelay;

    public Camera(String fileToRead, long imageStillDelayMs) {
        this.fileToRead = Objects.requireNonNull(fileToRead);
        
        // TBD: random 1 sec delay minimum
        Preconditions.checkArgument(imageStillDelayMs >= 1000);
        this.imageStillDelay = imageStillDelayMs;
        
    }
    
    public int[] takeStillAsRGB(int width, int height, boolean keepPadding) {
        // TODO:
        
        return null;
    }
}
