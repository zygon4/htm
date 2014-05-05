
package htm.core;

import htm.Input;
import htm.InputReceiver;
import htm.OutputProvider;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class SynapseSet<T extends Input<?>> implements InputConductor {

    /**
     * This is the synapse overlap percentage required for the segment
     * to be considered.  If the segment does not reach this threshold
     * then its overlap is set to 0.
     */
    private static final double STIMULUS_OVERLAP_PCT        = 0.1;
    
    private final Collection<Synapse<T>> synapses;

    public SynapseSet(Collection<Synapse<T>> synapses) {
        this.synapses = synapses;
    }
    
    @Override
    public double getValue() {
        
        int connectedCount = 0;
        
        for (Synapse synapse : this.synapses) {
            synapse.activateNewInput();
            if (synapse.isConnected() && synapse.getOutput() != null && synapse.getOutput().isActive()) {
                connectedCount ++;
            }
        }
        
        double overlap = (double) connectedCount / this.synapses.size();
        
        if (overlap > 0.0 && overlap < STIMULUS_OVERLAP_PCT) {
            overlap = 0.0;
        }
        
        return overlap;
    }

    void addOutputProviders(Collection<OutputProvider> outputProviders) {
        for (Synapse syn : this.synapses) {
            outputProviders.add(syn);
        }
    }

    void addInputReceivers(Collection<InputReceiver> inputReceivers) {
        for (Synapse syn : this.synapses) {
            inputReceivers.add(syn);
        }
    }
    
    @Override
    public void provideFeedback(Feedback feedback) {
        for (Feedback.Type type : feedback.getTypes()) {
            switch (type) {
                case ACTIVE_DUTY_CYCLE:
                    for (Synapse synapse : this.synapses) {
                        synapse.increasePermanence();
                    }
                    break;
                case BOOST:
                    break;
                case NEGATIVE: // fall through
                case POSITIVE:
                    for (Synapse synapse : this.synapses) {
                        synapse.adjustPermanence();
                    }
                    break;
            }
        }
    }
}
