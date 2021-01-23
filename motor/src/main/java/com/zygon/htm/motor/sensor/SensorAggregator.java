package com.zygon.htm.motor.sensor;

import com.zygon.htm.core.AbstractScheduledServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 *
 * @author zygon
 */
public abstract class SensorAggregator extends AbstractScheduledServiceImpl
        implements Sensor, ReadingHandler<Reading> {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReadLock readLock = lock.readLock();
    private final WriteLock writeLock = lock.writeLock();

    private final Collection<Reading> currentValues = new ArrayList<>();
    private final String name;
    private Reading currentAggregate = null;

    public SensorAggregator(Settings settings, String name) {
        super(settings);
        this.name = name;
    }

    // TODO: pass in a function vs abstract method
    protected abstract Reading aggregate(Collection<Reading> values);

    @Override
    protected void doRun() throws Exception {
        this.writeLock.lock();

        try {
            if (!this.currentValues.isEmpty()) {
                this.currentAggregate = this.aggregate(this.currentValues);
                this.currentValues.clear();
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public void doStart() throws SensorLifecycleException {
        // do nothing
    }

    @Override
    public void doStop() throws SensorLifecycleException {
        // do nothing
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Reading getReading() {
        this.readLock.lock();

        try {
            return this.currentAggregate;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public com.zygon.htm.motor.sensor.State getState() {
        return com.zygon.htm.motor.sensor.State.activated;
    }

    @Override
    public void handle(String sensorName, Reading value) {
        this.writeLock.lock();

        try {
            if (this.currentValues != null) {
                this.currentValues.add(new Reading(value.getValue(), System.currentTimeMillis()));
            }
        } finally {
            this.writeLock.unlock();
        }
    }
}
