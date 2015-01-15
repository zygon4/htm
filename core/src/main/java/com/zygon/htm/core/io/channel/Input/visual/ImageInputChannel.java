
package com.zygon.htm.core.io.channel.Input.visual;

import com.google.common.collect.Lists;
import com.zygon.htm.core.io.Input;
import com.zygon.htm.core.io.channel.Input.InputChannel;
import com.zygon.htm.core.io.input.image.ImageInputSet;
import com.zygon.htm.core.io.input.image.ImageUtil;
import com.zygon.htm.core.io.input.image.PixelInput;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author zygon
 */
public class ImageInputChannel extends InputChannel<ImageInputSet> {

    public static int getValue(int[] argb) {
        
        int red = argb[0] << 16;
        int green = argb[1] << 8;
        int blue = argb[2];
        
        return (red | green | blue) & 0xFFFFFF;
    }
    
    public static int[] getRGB (int argb) {
        return new int[] {
            (argb >> 16) & 0xff, //red
            (argb >>  8) & 0xff, //green
            (argb      ) & 0xff  //blue
        };
    }
    
    private static BufferedImage getImageFromFile(File image) throws FileNotFoundException, IOException {
        ImageInputStream iis = new FileImageInputStream(image);
        BufferedImage img = null;
        
        try {
            img = ImageIO.read(image);
        } finally {
            if (img == null) {
                try { iis.close(); } catch (IOException ignore) {}
            }
        }
        
        return img;
    }
    
    private static Settings createSettings() {
        Settings settings = new Settings();
        
        settings.setPeriod(10);
        settings.setTimeUnit(TimeUnit.SECONDS);
        
        return settings;
    }
    
    private int fileIdx = 0;
    private final ArrayList<File> files;

    public ImageInputChannel(Collection<File> files) {
        super(createSettings());
        
        if (files == null) {
            throw new IllegalArgumentException("Files cannot be null");
        }
        
        this.files = Lists.newArrayList(files);
    }

    public ImageInputChannel(File file) {
        this(Lists.newArrayList(file));
    }

    @Override
    public ImageInputSet getValue() {
        
        if (this.fileIdx == this.files.size()) {
            // TODO: document expectations
            return null;
        }
        
        File file = this.files.get(this.fileIdx++);
        
        ImageInputSet inputSet = null;
        
        try {
            inputSet = new ImageInputSet(file);
            
            System.out.println(new Date() + ": Read file: " + file.getAbsolutePath());
            
        } catch (IOException io) {
            io.printStackTrace();
            // TODO: ?? is this right?
            return null;
        }
        
        Collection<PixelInput> addInputs = null;
        
        try {
            addInputs = ImageInputSet.addInputs(file);
        } catch (IOException io) {
            io.printStackTrace();
        }
        
//        Collection<PixelInput> pixels = Lists.newArrayList();
//        Collections.addAll(pixels, addInputs.toArray(new PixelInput[pixels.size()]));
//
//        pixels = draw(2448, 3264, pixels);
//        
//        Collections.addAll(pixels, addInputs.toArray(new PixelInput[pixels.size()]));
//
//        try {
//            ImageUtil.writeImage(2448, 3264, pixels, "/tmp/erin_"+file.getName()+".png");
//        } catch (IOException io) {
//            io.printStackTrace();
//        }
        
        return inputSet;
    }
    
    private static void addPixels (int width, int height, int x, int y, Collection<PixelInput> pixels) {
        int xi = Math.max(0, x - 8);
        int yj = Math.max(0, y - 8);
        int xm = Math.min(width, x + 8);
        int ym = Math.min(height, y + 8);
        
        for (int i = xi; i < xm; i++) {
            for (int j = yj; j < ym; j++) {
                PixelInput in = new PixelInput(i, j);
                in.setValue(ImageInputSet.getRGB(0));
                pixels.add(in);
            }
        }
    }
    
    private static Collection<PixelInput> draw (int width, int height, Collection<PixelInput> pixelInputs) {
        
        List<PixelInput> inputs = Lists.newArrayList();
//        Set<String> inputCoords = Sets.newHashSet();
//        
//        int minx = Integer.MAX_VALUE;
//        int maxx = Integer.MIN_VALUE;
//        int miny = Integer.MAX_VALUE;
//        int maxy = Integer.MIN_VALUE;
//        int count = pixelInputs.size();
        
        for (PixelInput input : inputs) {
            int x1 = input.getX();
            int y1 = input.getY();
            addPixels(width, height, x1, y1, pixelInputs);
        }
        
        return pixelInputs;
    }
    
    private static void drawImage (Collection<PixelInput> values, String filepath) throws IOException {
        Collection<PixelInput> pixelInputs = Lists.newArrayList();
        int width = 0;
        int height = 0;
        
        for (Input<?> value : values) {
            PixelInput pixel = (PixelInput)value;
            int[] location = pixel.getLocation();
            int w = location[0];
            int h = location[1];
            width = Math.max(width, w);
            height = Math.max(height, h);
            
            pixelInputs.add(pixel);
        }
        
        Collection<PixelInput> drawnImage = draw(width, height, pixelInputs);
        ImageUtil.writeImage(width, height, drawnImage, filepath);
    }
}
