
package com.zygon.htm.sdr.core;

import com.google.common.collect.Queues;
import com.zygon.htm.sdr.Input;
import com.zygon.htm.sdr.InputReceiver;
import com.zygon.htm.sdr.OutputProvider;
import java.util.concurrent.BlockingQueue;

/**
 * 
 * @author david.charubini
 * @param <T>
 */
public class Synapse<T extends Input<?>> implements InputReceiver<T>, OutputProvider<T> {

    static final double CONNECTION_PERMENANCE = 0.5;
    static final double DEFAULT_PERMENANCE = 0.4;
    static final double PERMANENCE_ADJUSTMENT = 0.01;
    static final double MAX_PERMANENCE = 1.0;
    static final double MIN_PERMANENCE = 0.0;
    
    private final String id;
    private final BlockingQueue<T> inputBuffer = Queues.newArrayBlockingQueue(1000);
    private volatile T input;
    private volatile double permanence = DEFAULT_PERMENANCE;

    public Synapse(String id) {
        this.id = id; // TODO: UUID  +":"+String.valueOf(UUID.randomUUID().getLeastSignificantBits());
    }
    
    @Override
    public boolean isOutputActive() {
        return this.isConnected();
    }
    
    public boolean isConnected() {
        return this.permanence >= CONNECTION_PERMENANCE;
    }
    
    // I'm sketched out by the ratio of ups to downs -- log at some point
    public void adjustPermanence() {
        if (this.input != null) {
            if (this.input.isActive()) {
                this.permanence = Math.min(this.permanence + PERMANENCE_ADJUSTMENT, MAX_PERMANENCE);
            } else if (this.isConnected()) {
                this.permanence = Math.max(this.permanence - PERMANENCE_ADJUSTMENT, MIN_PERMANENCE);
            }
        }
    }

    @Override
    public T getOutput() {
        return this.input;
    }
    
    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void send(T input) {
        if (input == null) {
            throw new IllegalArgumentException();
        }
        
        this.inputBuffer.offer(input);
    }
    
    @Override
    public String toString() {
        return "Id: " + this.id + ", perm: " + String.format("%2f", this.permanence);
    }
    
    /*pkg*/ void increasePermanence() {
        this.permanence = Math.min(this.permanence + PERMANENCE_ADJUSTMENT, MAX_PERMANENCE);
    }

    /*pkg*/ void activateNewInput() {
        try {
            this.input = this.inputBuffer.take();
        } catch (InterruptedException intr) {
            // LOL log
            intr.printStackTrace();
        }
    }
}
