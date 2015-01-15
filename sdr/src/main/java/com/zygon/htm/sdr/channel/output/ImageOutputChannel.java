
package com.zygon.htm.sdr.channel.output;

import com.google.common.collect.Lists;
import com.zygon.htm.sdr.Input;
import com.zygon.htm.sdr.OutputProvider;
import com.zygon.htm.sdr.input.image.ImageInputSet;
import com.zygon.htm.sdr.input.image.ImageUtil;
import com.zygon.htm.sdr.input.image.PixelInput;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class ImageOutputChannel extends OutputChannel<ImageInputSet> {

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
    
    private static Settings createSettings() {
        Settings settings = new Settings();
        
        settings.setPeriod(10);
        settings.setTimeUnit(TimeUnit.SECONDS);
        
        return settings;
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
    
    public ImageOutputChannel() {
        super(createSettings());
    }

    @Override
    protected void doWrite(ImageInputSet values) {
        
//        String outputImg = "/tmp/"+values.toString()+"_img.png";
//        System.out.println("Writing out image to: " + outputImg);
//        try {
//            drawImage(values.getInputs(), outputImg);
//        } catch (IOException io) {
//            io.printStackTrace();
//        }
    }

    @Override
    protected ImageInputSet getOutput(Collection<OutputProvider> outputProviders) {
        
        Collection<PixelInput> outputs = Lists.newArrayList();
        
        for (OutputProvider val : outputProviders) {
            
            if (val.isOutputActive()) {
                Input value = val.getOutput();
                if (value != null && value.isActive()) {
                    // icky cast
                    outputs.add((PixelInput) value);
                    System.out.print("[X]");
                } else {
                    System.out.print("[_]");
                }
            } else {
                System.out.print("[ ]");
            }
        }
        
        System.out.println();
        
        ImageInputSet is = null;
        
//        try {
//            is = new ImageInputSet(outputs);
//        } catch (IOException io) {
//            // TODO:
//            io.printStackTrace();
//        }
        
        return is;
    }
}
