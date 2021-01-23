/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.motor.sensor.camera;

import java.util.concurrent.TimeUnit;
import nu.pattern.OpenCV;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author zygon
 */
public class OpenCVTester {

    static {
//        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
        OpenCV.loadLocally();
//        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        VideoCapture cap = new VideoCapture(0);
        //"http://192.168.1.7:2222"
        
        cap.open("rtsp://192.168.1.7:2222");
        
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));
        
        if (!cap.isOpened()) {
            System.err.println("didn't open!");
//            System.exit(1);
        }
        
//        if (!cap.open("tcp://192.168.1.7:2222")) {
//            System.err.println("didn't open!");
//        }
        //1280*720
        Mat frame = new Mat(720, 1280, CvType.CV_8UC1);
        cap.retrieve(frame);
        
//        cap.

        System.out.println(frame);
    }

}
