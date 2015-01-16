
package com.zygon.htm.core.io.channel.output;

import com.zygon.htm.core.io.InputSet;
import com.zygon.htm.core.io.OutputProvider;
import com.zygon.htm.core.AbstractScheduledServiceImpl;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author zygon
 * @param <T>
 */
public abstract class OutputChannel<T extends InputSet<?>> extends AbstractScheduledServiceImpl {
   
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
