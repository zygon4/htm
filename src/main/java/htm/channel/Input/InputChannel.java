
package htm.channel.Input;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import htm.Input;
import htm.InputReceiver;
import htm.InputSet;
import htm.channel.Channel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @author zygon
 */
public abstract class InputChannel<T extends InputSet<?>> extends Channel {
    
    // For mapping inputs to input receivers
    private Map<String, InputReceiver> inputReceiversByInputId = null;
    private volatile int synapseIdx = 0;
    
    // For use when adding a new input receiver to the map
    private ArrayList<InputReceiver> inputReceivers = null;
    
    public InputChannel(Settings settings) {
        super(settings);
    }
    
    public void addInputReceivers(Collection<InputReceiver> inputReceivers) {
        if (this.inputReceiversByInputId == null) {
            this.inputReceiversByInputId = Maps.newHashMap();
            
            this.inputReceivers = Lists.newArrayList(inputReceivers);
        }
    }
    
    @Override
    protected final void doRun() throws Exception {
        T val = this.getValue();
        
        if (val != null) {
            
            for (Input<?> input : val.getInputs()) {
                InputReceiver inputReceiver = this.inputReceiversByInputId.get(input.getId());
                
                if (inputReceiver == null) {
                    // select a new receiver for this input
                    inputReceiver = this.inputReceivers.get(this.synapseIdx++);
                    if (this.synapseIdx == this.inputReceivers.size()) {
                        this.synapseIdx = 0;
                    }
                    this.inputReceiversByInputId.put(input.getId(), inputReceiver);
                }
                
                inputReceiver.send(input);
            }
            
        } else {
            // for now..
            System.out.println("Received null value");
        }
    }
    
    public abstract T getValue();
}
