
package htm.channel.output;

import htm.InputSet;
import htm.OutputProvider;
import htm.channel.Channel;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author zygon
 */
public abstract class OutputChannel<T extends InputSet<?>> extends Channel {
   
    private Collection<OutputProvider> outputProviders = null;
    
    protected OutputChannel(Settings settings) {
        super(settings);
    }

    public void addOutputProviders(Collection<OutputProvider> outputProviders) {
        if (this.outputProviders == null) {
            this.outputProviders = Collections.unmodifiableCollection(outputProviders);
        }
    }
    
    protected abstract void doWrite(T values);
    
    protected abstract T getOutput(Collection<OutputProvider> outputProviders);
    
    @Override
    protected void doRun() throws Exception {
        T outputSet = getOutput(this.outputProviders);
        
        this.doWrite(outputSet);
    }
}
