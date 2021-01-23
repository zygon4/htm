/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.motor.sensor.camera;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;

/**
 *
 * This is POC. This is what turned into VideoCameraSensor.java.
 *
 * @author zygon
 */
public class USBCVTester extends JPanel {

    private BufferedImage image;

    public static void main(String args[]) throws InterruptedException {
        OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        USBCVTester tester = new USBCVTester();
        VideoCapture camera = new VideoCapture(0);

//        Mat frame = new Mat();
//        camera.read(frame);
        if (!camera.isOpened()) {
            System.out.println("Error");
        } else {
            JFrame frame0 = new JFrame();
            JFrame frame1 = new JFrame();

            while (true) {
                Mat frame = new Mat();

                if (camera.read(frame)) {
                    BufferedImage image = tester.MatToBufferedImage(frame);

//                    tester.window(frame0, image, "Original Image", 0, 0);
                    tester.window(frame1, tester.grayscale(image), "Processed Image", 40, 60);
//                    t.window(t.loadImage("ImageName"), "Image loaded", 0, 0);

                    for (int i = 0; i < image.getHeight(); i++) {
                        for (int j = 0; j < image.getWidth(); j++) {
                            Color c = new Color(image.getRGB(j, i));
                            int red = c.getRed();
                            int green = c.getGreen();
                            int blue = c.getBlue();
                            System.out.format("%d %d %d\n", red, green, blue);
                        }
                    }
                    break;
                }
            }
        }
        camera.release();
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public USBCVTester() {
    }

    public USBCVTester(BufferedImage img) {
        image = img;
    }

    //Show image on window
    public void window(JFrame frame, BufferedImage img, String text, int x, int y) {
//        JFrame frame = new JFrame();
        frame.getContentPane().add(new USBCVTester(img));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(text);
        frame.setSize(img.getWidth(), img.getHeight() + 30);
        frame.setLocation(x, y);
        frame.setVisible(true);
    }

    //Load an image
    public BufferedImage loadImage(String file) {
        BufferedImage img;

        try {
            File input = new File(file);
            img = ImageIO.read(input);

            return img;
        } catch (Exception e) {
            System.out.println("erro");
        }

        return null;
    }

    //Save an image
    public void saveImage(BufferedImage img) {
        try {
            File outputfile = new File("Images/new.png");
            ImageIO.write(img, "png", outputfile);
        } catch (Exception e) {
            System.out.println("error");
        }
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

    public BufferedImage MatToBufferedImage(Mat frame) {
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

}
