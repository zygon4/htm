
package htm.core;

import htm.Input;
import htm.InputProvider;
import htm.InputSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author david.charubini
 */
public class HTM extends Thread {

    private final InputProvider inputProvider;
    private final Pooler spatialPooler;
    private final Pooler temporalPooler;
    private final Region[][] regionsByLevel;
    
    private volatile boolean running = false;

    public HTM(InputProvider inputProvider, Region[][] regionsByLevel, Pooler spatialPooler, Pooler temporalPooler) {
        super();
        this.inputProvider = inputProvider;
        this.regionsByLevel = regionsByLevel;
        this.spatialPooler = spatialPooler;
        this.temporalPooler = temporalPooler;
    }

    public void initialize() {
        this.running = true;
        this.start();
    }
    
    @Override
    public void run() {
        while (this.running) {
            InputSet inputSet = null;
            try {
                inputSet = this.inputProvider.take();
            } catch (InterruptedException ie) {
                if (this.running) {
                    ie.printStackTrace();
                }
            }
            
            this.process(inputSet);
            // TODO: process output - via queue?
        }
    }
    
    public Collection<Input<?>> getConnectedInputs() {
        // TBD: take the connected synapses from the upper levels 
        // and feed downward?
        
        Collection<Input<?>> connectedInputs = new ArrayList<Input<?>>();
        
        for (Region region : this.regionsByLevel[0]) {
            region.getConnectedInputs(connectedInputs);
        }
        
        return connectedInputs;
    }
    
    // TODO: return output, feed region output into higher levels
    private void process (InputSet input) {
        
        System.out.println("Processing input");
        
        InputSet inputSet = input;
        
        for (Region[] regions : this.regionsByLevel) {
            
            Collection<Input<?>> connectedInputs = new ArrayList<Input<?>>();
            
            for (Region region : regions) {
                
                Collection<Input<?>> spatialInputs = new ArrayList<Input<?>>();
                
                // perform spatial pooling
                this.spatialPooler.process(region, inputSet);
                
                // pull out the connections
                region.getConnectedInputs(spatialInputs);
                
                // feed the connections into the temporal pooler
                this.temporalPooler.process(region, new InputSet(spatialInputs));
                
                // the remaining connections become input into the next level
                region.getConnectedInputs(connectedInputs);
            }
            
            inputSet = new InputSet(connectedInputs);
        }
    }
    
    public void uninitialize() {
        this.running = false;
    }
}
