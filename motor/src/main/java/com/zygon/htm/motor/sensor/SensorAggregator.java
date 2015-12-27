
package com.zygon.htm.motor.sensor;

import com.google.common.collect.Lists;
import com.zygon.htm.core.AbstractScheduledServiceImpl;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 *
 * @author zygon
 * @param <V>
 */
public abstract class SensorAggregator<V> extends AbstractScheduledServiceImpl implements Sensor<V>, ValueHandler<V> {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReadLock readLock = lock.readLock();
    private final WriteLock writeLock = lock.writeLock();

    private final Collection<Reading<V>> currentValues = Lists.newArrayList();
    private Reading<V> currentAggregate = null;

    public SensorAggregator(Settings settings) {
        super(settings);
    }

    protected abstract Reading<V> aggregate (Collection<Reading<V>> values);

    @Override
    protected void doRun() throws Exception {
        this.writeLock.lock();

        try {
            this.currentAggregate = this.aggregate(this.currentValues);
            this.currentValues.clear();
        } finally {
            this.writeLock.unlock();
        }
    }

    @Override
    public Reading<V> getReading() {
        this.readLock.lock();

        try {
            return this.currentAggregate;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void handle(V value) {
        this.writeLock.lock();

        try {
            this.currentValues.add(new Reading<>(value, System.currentTimeMillis()));
        } finally {
            this.writeLock.unlock();
        }
    }
}
