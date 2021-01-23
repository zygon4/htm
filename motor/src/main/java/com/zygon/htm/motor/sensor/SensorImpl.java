/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.motor.sensor;

import com.google.common.base.Preconditions;

/**
 *
 * @author zygon
 */
public abstract class SensorImpl implements Sensor {

    private State state = State.idle;

    @Override
    public final State getState() {
        return state;
    }

    @Override
    public final void doStart() throws SensorLifecycleException {
        Preconditions.checkState(state == State.idle || state == State.errored, 
                "Cannot start while in state " + state.name());

        state = State.activating;
        try {
            activate();
            state = State.activated;
        } catch (SensorLifecycleException se) {
            state = State.errored;
            throw se;
        }
    }

    protected abstract void activate() throws SensorLifecycleException;
    
    @Override
    public final void doStop() throws SensorLifecycleException {
        try {
            deactivate();
            state = State.idle;
        } catch (SensorLifecycleException se) {
            // TBD: keep the previous state? it's somewhat invariant at this point
            state = State.errored;
            throw se;
        }
    }
    
    protected abstract void deactivate() throws SensorLifecycleException;

//    @Override
//    public <T extends Reading> T getReading() throws SensorReadingException {
//        
//    }
//    
//    protected abstract <T extends Reading> T reading() throws SensorReadingException;
}
