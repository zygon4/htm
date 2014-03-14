package htm.input.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.imageio.ImageIO;

/**
 *
 * @author david.charubini
 */
public class ImageUtil {

    public static void writeImage(int width, int height, Collection<PixelInput> pixels, String filepath) throws IOException {

        BufferedImage img = new BufferedImage(width + 1, height + 1, BufferedImage.TYPE_INT_BGR);

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                img.setRGB(x, y, 0xFFFFFFFF);
            }
        }
        
        for (PixelInput pixel : pixels) {
            int[] value = pixel.getValue();
            int rgb = ImageInputSet.getValue(value);
            try {
                img.setRGB(pixel.getX(), pixel.getY(), rgb);
            } catch (Throwable re) {
                re.printStackTrace();
            }
        }
        
        File outputfile = new File(filepath);
        ImageIO.write(img, "png", outputfile);
    }
}
