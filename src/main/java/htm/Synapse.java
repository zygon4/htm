
package htm;

import htm.input.Input;

/**
 *
 * @author david.charubini
 */
public class Synapse implements InputReceiver {

    static final double CONNECTION_PERMENANCE = 0.5;
    static final double DEFAULT_PERMENANCE = 0.4;
    static final double PERMANENCE_ADJUSTMENT = 0.01;
    static final double MAX_PERMANENCE = 1.0;
    static final double MIN_PERMANENCE = 0.0;
    
    private final String inputId;
    private Input<?> input;
    private double permanence = DEFAULT_PERMENANCE;

    public Synapse(String inputId) {
        this.inputId = inputId;
    }

    public boolean isActive() {
        return this.input.isActive();
    }
    
    public boolean isConnected() {
        return this.permanence >= CONNECTION_PERMENANCE;
    }
    
    // I'm sketched out by the ratio of ups to downs -- log at some point
    public void adjustPermanence() {
        if (this.isActive()) {
            this.permanence = Math.min(this.permanence + PERMANENCE_ADJUSTMENT, MAX_PERMANENCE);
        } else if (this.isConnected()) {
            this.permanence = Math.max(this.permanence - PERMANENCE_ADJUSTMENT, MIN_PERMANENCE);
        }
    }

    /*pkg*/ Input<?> getInput() {
        return this.input;
    }

    @Override
    public String getId() {
        return this.inputId;
    }

    @Override
    public void send(Input<?> input) {
        if (!input.getId().equals(this.inputId)) {
            throw new IllegalArgumentException();
        }
        
        this.input = input;
    }
}
