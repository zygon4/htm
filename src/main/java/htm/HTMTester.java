
package htm;

import htm.input.Input;
import htm.input.InputSet;
import htm.input.image.ImageInputSet;
import htm.input.image.ImageUtil;
import htm.input.image.PixelInput;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
    
    public static void main(String[] args) throws IOException {
        FilenameFilter filter = new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return dir.isDirectory() && name.endsWith(".jpg");
            }
        };
        
        File inputDir = new File("/home/davec/src/lab/main/htm/input/image/erin");
        File[] inputFiles = inputDir.listFiles(filter);
        List<ImageInputSet> inputs = new ArrayList<ImageInputSet>();
        
        for (File input : inputFiles) {
            long start = System.currentTimeMillis();
            inputs.add(new ImageInputSet(input));
            long end = System.currentTimeMillis();
            System.out.println("loading image " + input.getAbsolutePath() + " took " + (end-start));
        }
        
        InputProvider inputProvider = new InputProvider();
        Region[][] regionsByLevel = { { createRegion(128, 50, inputs.get(0)) } };
        
        HTM htm = new HTM(inputProvider, regionsByLevel);
        
        htm.initialize();
        
        for (int i = 0; i < 100; i++) {
            for (InputSet file : inputs) {

                try {
                    inputProvider.put(file);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        
        System.in.read();
        
        Collection<Input<?>> connectedInputs = htm.getConnectedInputs();
        
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
        
        ImageUtil.writeImage(width, height, pixelInputs, "/tmp/erin.png");
        
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
}
