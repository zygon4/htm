package htm.core;

import org.junit.Test;

/**
 *
 * @author zygon
 */
public class SegmentTest {

    private static int synapseId = 0;
    
    private static Synapse createSegment() {
        Synapse syn = new Synapse(String.valueOf(synapseId++));
        syn.send(new BooleanInput(syn.getId()));
        return syn;
    }
    
    @Test
    public void testBoost() {
        
    }

    @Test
    public void testGetOverlapDutyCycle() {
        
    }

    @Test
    public void testGetConnectedInputs() {
        
    }

    @Test
    public void testIsActive() {
        
    }

    @Test
    public void testIsOverlapGreaterThanLocal() {
        
    }

    @Test
    public void testLearn() {
        
    }
}
