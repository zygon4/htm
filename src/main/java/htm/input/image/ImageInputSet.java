
package htm.input.image;

import htm.input.Input;
import htm.input.InputSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

/**
 *
 * @author david.charubini
 */
public class ImageInputSet extends InputSet {
    
    // additional constructor for file array
    public ImageInputSet(File image) throws IOException {
        super (addInputs(image));
    }
    
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
    
    public static Collection<Input<?>> addInputs(File image) throws IOException {
        
        BufferedImage img = null;
        try {
            img = getImageFromFile(image);
        } catch (IOException io) {
            io.printStackTrace();
            throw new RuntimeException(io);
        }
        
        Collection<Input<?>> inputs = new ArrayList<Input<?>>();
        
        int width = img.getWidth();
        int height = img.getHeight();
        
        for (int i = 0; i < height; i+=16) {
            for (int j = 0; j < width; j+=16) {
                int[] rgb = getRGB(img.getRGB(j, i));
                PixelInput pixel = new PixelInput(j, i);
                pixel.setValue(rgb);
                if (pixel.isActive()) {
                    inputs.add(pixel);
                }
            }
        }
        
        return inputs;
    }
}
