
package htm.driver;

import com.google.common.collect.Lists;
import htm.InputReceiver;
import htm.OutputProvider;
import htm.core.IHTM;
import htm.channel.Input.InputChannel;
import htm.channel.Input.RandomInputChannel;
import htm.channel.output.ImageOutputChannel;
import htm.channel.output.OutputChannel;
import htm.channel.output.PrintOutputChannel;
import htm.core.HTM;
import htm.core.Region;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author zygon
 */
public class HTMDriver {

    private final IHTM htm;
    private final Collection<InputChannel> inputChannels;
    private final Collection<OutputChannel> outputChannels;

    public HTMDriver(IHTM htm, Collection<InputChannel> inputAdapters, Collection<OutputChannel> outputAdapter) {
        super();
        this.htm = htm;
        this.inputChannels = inputAdapters;
        this.outputChannels = outputAdapter;
    }
    
    protected void startUp() {
        
        for (OutputChannel oc : this.outputChannels) {
            this.htm.register(oc);
            oc.startAsync();
        }
        
        for (InputChannel ic : this.inputChannels) {
            this.htm.register(ic);
            ic.startAsync();
        }
        
        this.htm.initialize();
    }

    protected void shutDown() {
        
        this.htm.uninitialize();
        
        for (InputChannel input : this.inputChannels) {
            input.stopAsync();
        }
        
        for (OutputChannel output : this.outputChannels) {
            output.stopAsync();
        }
    }
    
    private static final String CORE_PATH = "/home/zygon/src/github/htm";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        
        Collection<InputReceiver> inputReceivers = Lists.newArrayList();
        Collection<OutputProvider> outputProviders = Lists.newArrayList();
        
        Region region =  Region.createRegion(3, 3, inputReceivers, outputProviders);
        
        HTM htm = new HTM(region, inputReceivers, outputProviders);
        
//        FilenameFilter filter = new FilenameFilter() {
//
//            @Override
//            public boolean accept(File dir, String name) {
//                return dir.isDirectory() && name.endsWith(".jpg");
//            }
//        };
//
//        File inputDir = new File(CORE_PATH + "/src/main/java/htm/input/image/erin");
//        List<File> inputFiles = Lists.newArrayList();
//        Collections.addAll(inputFiles, inputDir.listFiles(filter));
//        Collections.shuffle(inputFiles);
//        
//        InputChannel ic = new ImageInputChannel(inputFiles);
        InputChannel ic = new RandomInputChannel();
        
//        OutputChannel oc = new ImageOutputChannel();
        OutputChannel oc = new PrintOutputChannel();
        
        HTMDriver driver = new HTMDriver(htm, Lists.newArrayList(ic), Lists.newArrayList(oc));
        
        driver.startUp();
        
        Thread.sleep (1000 * 60 * 8);
        
        driver.shutDown();
    }
}
