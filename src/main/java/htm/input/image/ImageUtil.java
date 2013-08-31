package htm.input.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import javax.imageio.ImageIO;

/**
 *
 * @author david.charubini
 */
public class ImageUtil {

    public static class FastConvexHull {

        public ArrayList<PixelInput> execute(ArrayList<PixelInput> points) {
            ArrayList<PixelInput> xSorted = (ArrayList<PixelInput>) points.clone();
            Collections.sort(xSorted, new XCompare());

            int n = xSorted.size();

            PixelInput[] lUpper = new PixelInput[n];

            lUpper[0] = xSorted.get(0);
            lUpper[1] = xSorted.get(1);

            int lUpperSize = 2;

            for (int i = 2; i < n; i++) {
                lUpper[lUpperSize] = xSorted.get(i);
                lUpperSize++;

                while (lUpperSize > 2 && !rightTurn(lUpper[lUpperSize - 3], lUpper[lUpperSize - 2], lUpper[lUpperSize - 1])) {
                    // Remove the middle point of the three last
                    lUpper[lUpperSize - 2] = lUpper[lUpperSize - 1];
                    lUpperSize--;
                }
            }

            PixelInput[] lLower = new PixelInput[n];

            lLower[0] = xSorted.get(n - 1);
            lLower[1] = xSorted.get(n - 2);

            int lLowerSize = 2;

            for (int i = n - 3; i >= 0; i--) {
                lLower[lLowerSize] = xSorted.get(i);
                lLowerSize++;

                while (lLowerSize > 2 && !rightTurn(lLower[lLowerSize - 3], lLower[lLowerSize - 2], lLower[lLowerSize - 1])) {
                    // Remove the middle point of the three last
                    lLower[lLowerSize - 2] = lLower[lLowerSize - 1];
                    lLowerSize--;
                }
            }

            ArrayList<PixelInput> result = new ArrayList<PixelInput>();

            for (int i = 0; i < lUpperSize; i++) {
                result.add(lUpper[i]);
            }

            for (int i = 1; i < lLowerSize - 1; i++) {
                result.add(lLower[i]);
            }

            return result;
        }

        private boolean rightTurn(PixelInput a, PixelInput b, PixelInput c) {
            return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX()) > 0;
        }

        private class XCompare implements Comparator<PixelInput> {

            @Override
            public int compare(PixelInput o1, PixelInput o2) {
                return (new Integer(o1.getX())).compareTo(new Integer(o2.getX()));
            }
        }
    }

    public static void writeImage(int width, int height, Collection<PixelInput> pixels, String filepath) throws IOException {

        BufferedImage img = new BufferedImage(width + 1, height + 1, BufferedImage.TYPE_INT_BGR);

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                img.setRGB(x, y, 0xFFFFFFFF);
            }
        }
        
//        ArrayList<PixelInput> hulled = new FastConvexHull().execute(new ArrayList<PixelInput>(pixels));
        
        for (PixelInput pixel : pixels) {
            int[] value = pixel.getValue();
            int rgb = ImageInputSet.getValue(value);
            try {
                img.setRGB(pixel.getX(), pixel.getY(), rgb);
            } catch (RuntimeException re) {
                re.printStackTrace();
            }
        }
        
        File outputfile = new File(filepath);
        ImageIO.write(img, "png", outputfile);
    }
}
