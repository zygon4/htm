
package com.zygon.htm.sdr.driver;

import com.google.common.collect.Lists;
import com.zygon.htm.core.io.InputReceiver;
import com.zygon.htm.core.io.OutputProvider;
import com.zygon.htm.sdr.core.IHTM;
import com.zygon.htm.core.io.channel.Input.InputChannel;
import com.zygon.htm.core.io.channel.Input.RandomInputChannel;
import com.zygon.htm.core.io.channel.output.ImageOutputChannel;
import com.zygon.htm.core.io.channel.output.OutputChannel;
import com.zygon.htm.core.io.channel.output.PrintOutputChannel;
import com.zygon.htm.sdr.core.HTM;
import com.zygon.htm.sdr.core.Region;
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
