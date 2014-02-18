
package htm.core;

import htm.Input;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author zygon
 */
public class SynapseTest {

    private static class TestInput extends Input<Boolean> {

        public TestInput(String id) {
            super(id);
        }

        @Override
        public String getDisplayString() {
            return "testInput";
        }

        @Override
        public boolean isActive() {
            return this.getValue() == Boolean.TRUE;
        }
    }
    
    @Test
    public void testDefaultConnection() {
        Synapse synapse = new Synapse("testid");
        
        if (Synapse.CONNECTION_PERMENANCE > Synapse.DEFAULT_PERMENANCE) {
            Assert.assertFalse(synapse.isConnected());
        }
        
        if (Synapse.CONNECTION_PERMENANCE <= Synapse.DEFAULT_PERMENANCE) {
            Assert.assertTrue(synapse.isConnected());
        }
    }
    
    @Test
    public void testBecomeConnected() {
        Synapse synapse = new Synapse("testid");
        
        Assert.assertFalse(synapse.isConnected());
        
        TestInput testInput = new TestInput("testid");
        testInput.setValue(Boolean.TRUE);
        
        Assert.assertEquals(Boolean.TRUE, testInput.getValue());
        
        synapse.send(testInput);
        
        Assert.assertTrue(synapse.isActive());
        
        double adjustmentsUntilConnection = Math.round((Synapse.CONNECTION_PERMENANCE - Synapse.DEFAULT_PERMENANCE) / Synapse.PERMANENCE_ADJUSTMENT);
        
        for (int i = 0; i < adjustmentsUntilConnection; i++) {
            Assert.assertFalse(synapse.isConnected());
            synapse.adjustPermanence();
        }
        
        Assert.assertTrue(synapse.isConnected());
    }
    
    @Test
    public void testLooseConnected() {
        Synapse synapse = new Synapse("testid");
        
        Assert.assertFalse(synapse.isConnected());
        
        TestInput testInput = new TestInput("testid");
        testInput.setValue(Boolean.TRUE);
        
        Assert.assertEquals(Boolean.TRUE, testInput.getValue());
        
        synapse.send(testInput);
        
        Assert.assertTrue(synapse.isActive());
        
        double adjustmentsUntilConnection = Math.round((Synapse.CONNECTION_PERMENANCE - Synapse.DEFAULT_PERMENANCE) / Synapse.PERMANENCE_ADJUSTMENT);
        
        for (int i = 0; i < adjustmentsUntilConnection; i++) {
            Assert.assertFalse(synapse.isConnected());
            synapse.adjustPermanence();
        }
        
        Assert.assertTrue(synapse.isConnected());
        
        // unwind
        
        testInput.setValue(Boolean.FALSE);
        
        Assert.assertFalse(synapse.isActive());
        
        synapse.adjustPermanence();
        
        Assert.assertFalse(synapse.isConnected());
    }
}
