
package htm.core;

import htm.InputReceiver;
import htm.OutputProvider;
import htm.channel.Input.InputChannel;
import htm.channel.output.OutputChannel;
import java.util.Collection;

/**
 *
 * @author david.charubini
 */
public class HTM implements IHTM {

    // Single region for now
    private final Region region;
    
    // Having these here might be a hack
    private final Collection<InputReceiver> inputReceivers;
    private final Collection<OutputProvider> outputProviders;
    
    public HTM(Region regionsByLevel, Collection<InputReceiver> inputReceivers, Collection<OutputProvider> outputProviders) {
        super();
        
        this.region = regionsByLevel;
        this.inputReceivers = inputReceivers;
        this.outputProviders = outputProviders;
    }
    
    @Override
    public void initialize() {
        this.region.initialize();
    }
    
    @Override
    public void register(InputChannel inputChannel) {
        inputChannel.addInputReceivers(this.inputReceivers);
    }

    @Override
    public void register(OutputChannel outputChannel) {
        outputChannel.addOutputProviders(this.outputProviders);
    }
    
    @Override
    public void uninitialize() {
        this.region.uninitialize();
    }
}
