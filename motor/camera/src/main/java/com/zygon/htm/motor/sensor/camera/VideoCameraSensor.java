/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.motor.sensor.camera;

import com.zygon.htm.motor.sensor.Reading;
import com.zygon.htm.motor.sensor.SensorImpl;
import com.zygon.htm.motor.sensor.SensorLifecycleException;
import com.zygon.htm.motor.sensor.SensorReadingException;
import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

/**
 * Not thread safe.
 *
 * @author zygon
 */
public class VideoCameraSensor extends SensorImpl {

    static {
        OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private final String name;
    private VideoCapture camera = null;

    public VideoCameraSensor(String name) {
        this.name = name;
    }

    @Override
    protected void activate() throws SensorLifecycleException {
        if (camera != null) {
            throw new SensorLifecycleException(getClass().getSimpleName() + " is still active");
        }

        camera = new VideoCapture(0);
    }

    @Override
    protected void deactivate() throws SensorLifecycleException {
        if (camera == null) {
            throw new SensorLifecycleException(getClass().getSimpleName() + " is not active");
        }

        camera.release();
        camera = null;
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
    public Reading getReading() throws SensorReadingException {
        Mat frame = new Mat();
        camera.read(frame);

        byte[] data = new byte[frame.height() * frame.width()];
        int dataIdx = 0;
        BufferedImage img = grayscale(matToBufferedImage(frame));
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color c = new Color(img.getRGB(j, i));
                // in grayscale, RGB are all the same.
                byte red = (byte) c.getRed();
                data[dataIdx++] = red;
            }
        }

        return new Reading(data, System.currentTimeMillis());
    }

    private BufferedImage matToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);

        return image;
    }

    //Grayscale filter
    public BufferedImage grayscale(BufferedImage img) {
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color c = new Color(img.getRGB(j, i));

                int red = (int) (c.getRed() * 0.299);
                int green = (int) (c.getGreen() * 0.587);
                int blue = (int) (c.getBlue() * 0.114);

                Color newColor
                        = new Color(
                                red + green + blue,
                                red + green + blue,
                                red + green + blue);

                img.setRGB(j, i, newColor.getRGB());
            }
        }

        return img;
    }

}
