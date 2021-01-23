package com.zygon.htm.motor.sensor.impl;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.media.CannotRealizeException;
import javax.media.Control;
import javax.media.Format;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.NoProcessorException;
import javax.media.Player;
import javax.media.control.FormatControl;
import javax.media.control.FrameRateControl;

/**
 *
 * @author zygon
 */
public class SensorTester {

    private static final String CORE_PATH = "/home/zygon/src/github/htm/core";

     /* Constants */
//    private static final String CAPTURE_FORMAT = VideoFormat.;
    
//    public static final float FRAME_RATE = (float) 29.9; // fps
    
    // Video format: RGB, 640x480, 24 bit
    private static final int FORMAT_INDEX = 10; // the index is obtained from JMF Registry
    
    private static Collection<File> getImageFiles() {
        FilenameFilter filter = (File dir, String name) -> dir.isDirectory() && name.endsWith(".jpg");

        File inputDir = new File(CORE_PATH + "/src/main/java/com/zygon/htm/core/io/input/image/erin");
        List<File> inputFiles = Lists.newArrayList();
        Collections.addAll(inputFiles, inputDir.listFiles(filter));
        Collections.shuffle(inputFiles);

        return inputFiles;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, NoProcessorException, NoPlayerException, CannotRealizeException, InterruptedException {
//        getImageFiles();

//        File file = new File("/home/zygon/vid1.mpg");
        File file = new File("/home/zygon/round1.avi");

        Player player = Manager.createPlayer(new MediaLocator(file.toURL()));
        try {
            
//            Component videoScreen = player.getVisualComponent();
//            Frame frame = new Frame();
//            frame.setBounds(10, 10, 300, 300);
//            frame.add(videoScreen);
//            frame.setVisible(true);
            
            player.start();
            Thread.sleep(5000);
            
            
            Control[] controls = player.getControls();
            
            FormatControl fc = (FormatControl) player.getControl(
                    "javax.media.control.FormatControl");
            Format[] formats = fc.getSupportedFormats();
            
//            fc.setFormat(formats[FORMAT_INDEX]);
            
            System.out.println(fc.getFormat());
            
            
            Thread.sleep(5000);
            
            FrameRateControl frc = (FrameRateControl) player
                    .getControl("javax.media.control.FrameRateControl");
            
//            frc.setFrameRate(FRAME_RATE);
            
            if (frc != null) {
                System.out.println("Frame rate is: " + frc.getFrameRate());
            }
            
            System.out.println("done with creating the realized player");
            
            
//        VideoFormat videoFormat = (VideoFormat)frameBuffer.getFormat(); 
            
            Thread.sleep(5000);
        } finally {
            player.stop();
        }
        
        System.exit(0);
    }
}
