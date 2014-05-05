
package htm.core;

import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author zygon
 */
public class SynapseTest {
    
    @Test
    public void testDefaultConnection() {
        Synapse synapse = new Synapse("id");
        
        if (Synapse.CONNECTION_PERMENANCE > Synapse.DEFAULT_PERMENANCE) {
            Assert.assertFalse(synapse.isConnected());
        }
        
        if (Synapse.CONNECTION_PERMENANCE <= Synapse.DEFAULT_PERMENANCE) {
            Assert.assertTrue(synapse.isConnected());
        }
    }
    
    @Test
    public void testBecomeConnected() {
        Synapse synapse = new Synapse("id");
        
        Assert.assertFalse(synapse.isConnected());
        
        BooleanInput testInput = new BooleanInput("testid");
        testInput.setValue(Boolean.TRUE);
        
        Assert.assertEquals(Boolean.TRUE, testInput.getValue());
        
        synapse.send(testInput);
        
        Assert.assertTrue(synapse.getOutput().isActive());
        
        double adjustmentsUntilConnection = Math.round((Synapse.CONNECTION_PERMENANCE - Synapse.DEFAULT_PERMENANCE) / Synapse.PERMANENCE_ADJUSTMENT);
        
        for (int i = 0; i < adjustmentsUntilConnection; i++) {
            Assert.assertFalse(synapse.isConnected());
            synapse.adjustPermanence();
        }
        
        Assert.assertTrue(synapse.isConnected());
    }
    
    @Test
    public void testLooseConnected() {
        Synapse synapse = new Synapse("id");
        
        Assert.assertFalse(synapse.isConnected());
        
        BooleanInput testInput = new BooleanInput("testid");
        testInput.setValue(Boolean.TRUE);
        
        Assert.assertEquals(Boolean.TRUE, testInput.getValue());
        
        synapse.send(testInput);
        
        Assert.assertTrue(synapse.getOutput().isActive());
        
        double adjustmentsUntilConnection = Math.round((Synapse.CONNECTION_PERMENANCE - Synapse.DEFAULT_PERMENANCE) / Synapse.PERMANENCE_ADJUSTMENT);
        
        for (int i = 0; i < adjustmentsUntilConnection; i++) {
            Assert.assertFalse(synapse.isConnected());
            synapse.adjustPermanence();
        }
        
        Assert.assertTrue(synapse.isConnected());
        
        // unwind
        
        testInput.setValue(Boolean.FALSE);
        
        Assert.assertFalse(synapse.getOutput().isActive());
        
        synapse.adjustPermanence();
        
        Assert.assertFalse(synapse.isConnected());
    }
}
