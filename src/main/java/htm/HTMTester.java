
package htm;

import htm.input.image.ImageInputSet;
import htm.input.image.ImageUtil;
import htm.input.image.PixelInput;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author davec
 */
public class HTMTester {

    private static Segment createSegment(Collection<Input> inputs) {
        Collection<Synapse> synapses = new ArrayList<Synapse>();
        
        for (Input in : inputs) {
            synapses.add(new Synapse(in.getId()));
        }
        
        return new Segment(synapses);
    }
    
    private static Region createRegion (int columnCount, int feedForwardSynapseCount, InputSet inputSet) {
        
        Collection<Column> columns = new ArrayList<Column>();
        
        for (int i = 0; i < columnCount; i++) {
            Collection<Input> inputs = new ArrayList<Input>();
            
            inputSet.addInputs(inputs, feedForwardSynapseCount);
            
            columns.add(new Column("col_"+(i+1), createSegment(inputs), null));
        }
        
        return new Region(columns);
    }
    
    public static InputSet create (File file) throws IOException {
        return new ImageInputSet(file);
    }

    private static final String CORE_PATH = "/home/zygon/src/github/htm";

    public static void main(String[] args) throws IOException {
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return dir.isDirectory() && name.endsWith(".jpg");
            }
        };

        File inputDir = new File(CORE_PATH + "/src/main/java/htm/input/image/erin");
        List<File> inputFiles = new ArrayList<File>();
        Collections.addAll(inputFiles, inputDir.listFiles(filter));
        Collections.shuffle(inputFiles);
        
        List<ImageInputSet> inputSets = new ArrayList<ImageInputSet>();

        for (File input : inputFiles) {
            long start = System.currentTimeMillis();
            inputSets.add(new ImageInputSet(input));
            Collection<Input<?>> addInputs = ImageInputSet.addInputs(input);
            
            Collection<PixelInput> pixels = new ArrayList<PixelInput>();
            Collections.addAll(pixels, addInputs.toArray(new PixelInput[pixels.size()]));
            
            pixels = draw(2448, 3264, pixels);
            ImageUtil.writeImage(2448, 3264, pixels, "/tmp/erin_"+input.getName()+".png");
            
            long end = System.currentTimeMillis();
            System.out.println("loading image " + input.getAbsolutePath() + " took " + (end-start));
        }
        
        System.out.println("Done loading images\n");
        
        InputProvider inputProvider = new InputProvider();
        Region[][] regionsByLevel = { { createRegion(128, 50, inputSets.get(0)) } };
        
        HTM htm = new HTM(inputProvider, regionsByLevel);
        
        htm.initialize();
        
//        for (int i = 0; i < 100; i++) {
            for (ImageInputSet inputSet : inputSets) {

                try {
                    inputProvider.put(inputSet);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            
        
//                Thread.sleep (2000);
            System.out.println("Press anything to continue");
            System.in.read();

            drawImage(htm.getConnectedInputs(), "/tmp/"+inputSet.getImage().getName()+"_img.png");
        }
//        }
        
//        System.in.read();
//        
//        Collection<Input<?>> connectedInputs = htm.getConnectedInputs();
//        
//        Collection<PixelInput> pixelInputs = new ArrayList<PixelInput>();
//        int width = 0;
//        int height = 0;
//        
//        for (Input<?> input : connectedInputs) {
//            PixelInput pixel = (PixelInput)input;
//            int[] location = pixel.getLocation();
//            int w = location[0];
//            int h = location[1];
//            width = Math.max(width, w);
//            height = Math.max(height, h);
//            
//            pixelInputs.add(pixel);
//        }
//        
//        Collection<PixelInput> drawnImage = draw(width, height, pixelInputs);
//        ImageUtil.writeImage(width, height, drawnImage, "/tmp/erin.png");
        
        System.in.read();

        htm.uninitialize();
        
//        for (int j = 0; j < 10; j++) {
//            
//            Collection<Column> activeColumns = new ArrayList<Column>();
//            
//            for (Column column : columns) {
//                column.process();
//                
//                boolean isActive = column.isActive();
//                boolean suppress = isActive && column.getOverlap() < 2;
//                
//                column.setSuppressed(suppress);
//                
//                isActive = column.isActive();
//                
//                if (isActive) {
//                    activeColumns.add(column);
//                }
//
//                System.out.printf("[%s]", isActive ? "+" : " ");
//            }
//            
//            for (Column activeColumn : activeColumns) {
//                activeColumn.learn();
//            }
//
//            System.out.println();
//        }
    }
    
    private static void drawImage (Collection<Input<?>> connectedInputs, String filepath) throws IOException {
        Collection<PixelInput> pixelInputs = new ArrayList<PixelInput>();
        int width = 0;
        int height = 0;
        
        for (Input<?> input : connectedInputs) {
            PixelInput pixel = (PixelInput)input;
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
        
        List<PixelInput> inputs = new ArrayList<>(pixelInputs);
        Set<String> inputCoords = new HashSet<String>();
        
        int minx = Integer.MAX_VALUE;
        int maxx = Integer.MIN_VALUE;
        int miny = Integer.MAX_VALUE;
        int maxy = Integer.MIN_VALUE;
        int count = pixelInputs.size();
        
        for (PixelInput input : inputs) {
            int x1 = input.getX();
            int y1 = input.getY();
            addPixels(width, height, x1, y1, pixelInputs);
        }
        
        return pixelInputs;
        
//        for (PixelInput input : pixelInputs) {
//            int x1 = input.getX();
//            int y1 = input.getY();
//            
//            if (x1 < minx) {
//                minx = x1;
//            }
//            if (x1 > maxx) {
//                maxx = x1;
//            }
//            if (y1 < miny) {
//                miny = y1;
//            }
//            if (y1 > maxy) {
//                maxy = y1;
//            }
//            
//            System.out.println(x1 + "/" + y1);
//            
//            for (PixelInput otherInput : pixelInputs) {
//                if (input != otherInput) {
//                    int x2 = otherInput.getX();
//                    int y2 = otherInput.getY();
//
//                    // draw a line
//                    // y = mx + b
//                    int ys = y2 - y1;
//                    int xs = x2 - x1;
//
//                    int slope = xs != 0 ? (ys / xs) : 0;
//
//                    for (int i = x1; i < x2 - 16; i+=16) {
//                        int y = y1 + slope *(i - x1);
//                        String key = i+""+y;
//                        if (!inputCoords.contains(key)) {
//                            PixelInput in = new PixelInput(i, y);
//                            in.setValue(ImageInputSet.getRGB(0));
//                            inputs.add(in);
//                            inputCoords.add(key);
//                        }
//                    }
//                }
//            }
//        }
//        
//        System.out.printf("count %d, minx %d, maxx %d, miny %d, maxy %d\n", 
//                count, minx, maxx, miny, maxy);
//        
//        return inputs;
    }
}
