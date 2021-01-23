/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.motor.sensor.camera;

import com.zygon.htm.motor.sensor.NetworkClientSensor;
import com.zygon.htm.motor.sensor.Reading;
import com.zygon.htm.motor.sensor.SensorReadingException;
import org.jcodec.codecs.h264.H264Decoder;
import org.jcodec.codecs.h264.H264Utils;
import org.jcodec.codecs.h264.MappedH264ES;
import org.jcodec.common.JCodecUtil;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Packet;
import org.jcodec.common.model.Picture;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 *
 * @author zygon
 */
public class NetworkCameraSensor extends NetworkClientSensor {

    private final String name;

    public NetworkCameraSensor(String remoteHost, int remotePort, int dataPacketLen, String name) {
        super(remoteHost, remotePort, dataPacketLen);
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String getName() {
        return super.getName() + "_" + name;
    }

    @Override
    public String getUnits() {
        return "pixel";
    }

    @Override
    public Reading getReading() throws SensorReadingException {
        Reading reading = super.getReading();
        H264Decoder decoder = new H264Decoder();

        // for.. debugging
        decoder.setDebug(true);

        ByteBuffer bb = ByteBuffer.wrap(reading.getValue());
        H264Utils.skipToNALUnit(bb);
        Picture pictureOut = Picture.create(1280, 720, ColorSpace.YUV420);

        MappedH264ES mapped = new MappedH264ES(bb);
        Packet nextFrame;
        while ((nextFrame = mapped.nextFrame()) != null) {
            Picture out = decoder.decodeFrame(nextFrame.getData(), pictureOut.getData()).cropped();

            try {
                JCodecUtil.savePictureAsPPM(pictureOut, new File("/tmp/image.file"));
            } catch (IOException io) {
                // TODO: log
                io.printStackTrace();
            }
        }

//        if (decoder.probe(bb) == 0) {
//        Picture real = decoder.decodeFrame(bb, out.getData());
//            try {
//                JCodecUtil.savePictureAsPPM(real, new File("/tmp/image.file"));
//            } catch (IOException io) {
//                // TODO: log
//                io.printStackTrace();
//            }
//            int[][] data = real.getData();
//        }
//        BufferedImage bi = JCodecUtil.toBufferedImage(real); // If you prefere AWT image
//        int[] stillAsRGB = camera.takeStillAsRGB(500, 500, true);
//
//        for (int i = 0; i < stillAsRGB.length; i++) {
//            int[] rgb = ImageUtils.getRGB(stillAsRGB[i]);
//
//        }
//        return null;
        return reading;
    }
}
