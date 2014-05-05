
package htm.core;

import com.google.common.util.concurrent.AtomicDouble;
import java.util.Collection;
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
    private static final double ACTIVE_NET_INPUT_MIN          = 0.25;
    
    /**
     * This is the constant by which this Segment increases its
     * local activity.  
     */
    private static final double BOOST                       = 0.01;
    
    /**
     * This is the minimum required running overlap ratio mean value.  If
     * the running overlap mean value drops below this, then the synapses
     * will be increased to stimulate connections.
     */
    private static final double MIN_NET_INPUT_DUTY          = -0.50;

    private final DescriptiveStatistics activeDutyCycle = new DescriptiveStatistics(1000);
    private final DescriptiveStatistics netInputDutyCycle = new DescriptiveStatistics(1000);
    
    private final AtomicDouble overlapRatio = new AtomicDouble(0.0);
    
    // Having the boost here is a bit sketchy - this sort of pushes this into 
    // the realm of being a proximal dendrite segment.
    private double boost = new Random().nextDouble();
    
    private volatile boolean isActive = false;
    
    private final int id;
    private InputConductor inputConductor = null;
    private InhibitionProvider inhibitionProvider = null;
    
    public Segment(int id, InputConductor inputConductor, InhibitionProvider inhibitionProvider) {
        this.id = id;
        // These can be set or passed in
        this.inputConductor = inputConductor;
        this.inhibitionProvider = inhibitionProvider;
    }
    
    private double getBoost(double activeDutyCycle, double minDutyCycle) {
        if (activeDutyCycle >= minDutyCycle) {
            return 1.0;
        } else {
            return this.boost + BOOST;
        }
    }

    public int getId() {
        return this.id;
    }

    public InhibitionProvider getInhibitionProvider() {
        return this.inhibitionProvider;
    }
    
    public InputConductor getInputConductor() {
        return this.inputConductor;
    }
    
    public boolean isActive() {
        return this.isActive;
    }
    
    public void process() {
        double excitation = this.inputConductor.getValue();
        double inhibition = this.inhibitionProvider.getValue();
        
        // TODO: move boost down as feedback?????
        // add boost if apppropriate
//        if (this.boost != 0.0 && excitation != 0.0) {
//            excitation *= this.boost;
//        }
        
        double netInputValue = excitation - inhibition;
        
        this.overlapRatio.set(netInputValue);
        
        // store the net input duty cycle if synapse stimulation is required during learning
        this.netInputDutyCycle.addValue(netInputValue);
        
        this.isActive = netInputValue >= ACTIVE_NET_INPUT_MIN;
        
        // store the active duty value for boosting later
        double meanActiveDutyCycle = this.activeDutyCycle.getMean();
        this.activeDutyCycle.addValue(this.isActive ? 1.0 : 0.0);
        
        if (this.isActive) {
            System.out.println(this.id + ") active, mean duty cycle: " + meanActiveDutyCycle + ", current net input: " + netInputValue);
        }
    }
    
    // "globallyActive" implies both spatial and temporal activity.
    public void learn(boolean globallyActive, Collection<Segment> localSegments) {
        Feedback feedback = new Feedback();
        
        if (globallyActive) {
            feedback.add(Feedback.Type.POSITIVE);
        } else {
            feedback.add(Feedback.Type.NEGATIVE);
        }
        
        // TODO: re-introduce boosting
        // calculate max duty cycle
//        double maxOverlapDutyCycle = 0.0;
//        
//        for (Segment local : localSegments) {
//            
//            double localOverlap = local.overlapRatio.get();
//            
//            if (localOverlap > maxOverlapDutyCycle) {
//                maxOverlapDutyCycle = localOverlap;
//            }
//        }
//        
//        // calculate the mix duty cycle
//        double minDutyCycle = 0.01 * maxOverlapDutyCycle;
//        this.boost = getBoost (this.activeDutyCycle.getMean(), minDutyCycle);
        
        // Check the overlap duty cycle and increase synapses.
        // If the synapses aren't coming connected enough to activate
        // this segment then this will help out.
        double netInputDuty = this.netInputDutyCycle.getMean();
        
        if (netInputDuty < MIN_NET_INPUT_DUTY) {
            feedback.add(Feedback.Type.ACTIVE_DUTY_CYCLE);
        }
        
        // TODO: boost feedback????
        
        this.inputConductor.provideFeedback(feedback);
    }

    public void setInhibitionProvider(InhibitionProvider inhibitionProvider) {
        if (this.inhibitionProvider != null) {
            throw new IllegalStateException();
        }
        this.inhibitionProvider = inhibitionProvider;
    }

    public void setInputConductor(InputConductor inputConductor) {
        if (this.inputConductor != null) {
            throw new IllegalStateException();
        }
        this.inputConductor = inputConductor;
    }
}
