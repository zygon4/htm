
package htm.core;

import htm.Input;
import htm.InputReceiver;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * A Segment contains a collection of synapses as input. It controls its
 * activity through them, by supplied local segments, and any local boosting
 * activity.
 *
 * @author david.charubini
 */
public class Segment {
    
    /**
     * This is the synapse overlap percentage required for the segment
     * to be considered "active".
     */
    private static final double ACTIVE_OVERLAP_PCT          = 0.25;
    
    /**
     * This is the constant by which this Segment increases its
     * local activity.  
     */
    private static final double BOOST                       = 0.01;
    
    /**
     * This is the synapse overlap percentage required for the segment
     * to be considered.  If the segment does not reach this threshold
     * then its overlap is set to 0.
     */
    private static final double STIMULUS_OVERLAP_PCT        = 0.1;
    
    /**
     * This is the minimum required running overlap ratio mean value.  If
     * the running overlap mean value drops below this, then the synapses
     * will be increased to stimulate connections.
     */
    private static final double MIN_OVERLAP_DUTY_PCT        = 0.01;

    private final DescriptiveStatistics activeDutyCycle = new DescriptiveStatistics(1000);
    private final DescriptiveStatistics overlapDutyCycle = new DescriptiveStatistics(1000);
    private final Collection<Synapse> synapses;
    
    private double overlapRatio = 0.0;
    
    // Having the boost here is a bit sketchy - this sort of pushes this into 
    // the realm of being a proximal dendrite segment.
    private double boost = new Random().nextDouble();
    
    private boolean isActive = false;
    
    public Segment(Collection<Synapse> synapses) {
        
        if (synapses.isEmpty()) {
            throw new IllegalArgumentException("must have at least 1 synapse");
        }
        
        this.synapses = Collections.unmodifiableCollection(synapses);
    }
    
    private double getBoost(double activeDutyCycle, double minDutyCycle) {
        if (activeDutyCycle >= minDutyCycle) {
            return 1.0;
        } else {
            return this.boost + BOOST;
        }
    }
    
    public void getConnectedInputs(Collection<Input<?>> connectedInputs) {
        for (Synapse syn : this.synapses) {
            if (syn.isConnected()) {
                Input<?> input = syn.getInput();
                connectedInputs.add(input);
            }
        }
    }
    
    public Collection<InputReceiver> getInputReceivers() {
        Collection<InputReceiver> inputReceivers = new ArrayList<InputReceiver>();
        
        for (Synapse syn : this.synapses) {
            inputReceivers.add(syn);
        }
        
        return inputReceivers;
    }
    
    public boolean isActive() {
        return this.isActive;
    }
    
    boolean isOverlapGreaterThanLocal(Collection<Segment> localSegments) {
        
        int greaterCount = 0;
        
        if (this.overlapRatio > 0) {
            for (Segment segment : localSegments) {
                if (this.overlapRatio > segment.overlapRatio) {
                    greaterCount ++;
                } else if (this.overlapRatio == segment.overlapRatio) {
                    // arbitrarily pick one
                    if (this.hashCode() > segment.hashCode()) {
                        greaterCount ++;
                    }
                }
            }
        }
        
        if (greaterCount == localSegments.size()) {
            return true;
        }
        
        return false;
    }
    
    private double calculateOverlap() {
        int connectedCount = 0;
        
        for (Synapse synapse : this.synapses) {
            if (synapse.isConnected() && synapse.isActive()) {
                connectedCount ++;
            }
        }
        
        double overlapRatio = (double) connectedCount / this.synapses.size();
        
        if (overlapRatio < STIMULUS_OVERLAP_PCT) {
            return 0.0;
        }
        
        return overlapRatio;
    }
    
    public void process() {
        
        // set the overlap ratio
        this.overlapRatio = this.calculateOverlap();
        
        // add boost if apppropriate
        if (this.boost != 0.0) {
            this.overlapRatio *= this.boost;
        }
        
        // store the overlap ratio if synapse stimulation is required during
        // learning
        this.overlapDutyCycle.addValue(this.overlapRatio);
        
        // set the activation
        this.isActive = this.overlapRatio >= ACTIVE_OVERLAP_PCT;
        
        // store the active duty value for boosting later
        this.activeDutyCycle.addValue(this.isActive ? 1.0 : 0.0);
    }
    
    public void learn(boolean isActive, Collection<Segment> localSegments) {
        if (isActive) {
            for (Synapse synapse : this.synapses) {
                synapse.adjustPermanence();
            }
        }
        
        // calculate max duty cycle
        double maxOverlapDutyCycle = 0.0;
        
        for (Segment local : localSegments) {
            if (local.overlapRatio > maxOverlapDutyCycle) {
                maxOverlapDutyCycle = local.overlapRatio;
            }
        }
        
        // calculate the mix duty cycle
        double minDutyCycle = 0.01 * maxOverlapDutyCycle;
        this.boost = getBoost (this.activeDutyCycle.getMean(), minDutyCycle);
        
        
        // Check the overlap duty cycle and increase synapses.
        // If the synapses aren't coming connected enough to activate
        // this segment then this will help out.
        double overlapDutyCyclePct = this.overlapDutyCycle.getMean();
        
        if (overlapDutyCyclePct < MIN_OVERLAP_DUTY_PCT) {
            for (Synapse synapse : this.synapses) {
                synapse.increasePermanence();
            }
        }
    }
}
