
package htm;

import htm.input.Input;
import htm.input.InputSet;
import htm.pooling.spatial.SpatialPooler;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author david.charubini
 */
public class HTM extends Thread {

    private final InputProvider inputProvider;
    private final SpatialPooler spatialPooler = new SpatialPooler();
    // todo: temporal pooler
    private final Region[][] regionsByLevel;
    
    private volatile boolean running = false;

    public HTM(InputProvider inputProvider, Region[][] regionsByLevel) {
        super();
        this.inputProvider = inputProvider;
        this.regionsByLevel = regionsByLevel;
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
                ie.printStackTrace();
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
        
        for (Region[] regions : this.regionsByLevel) {
            for (Region region : regions) {
                this.spatialPooler.process(region, input);
            }
        }
    }
    
    public void uninitialize() {
        this.running = false;
    }
}
