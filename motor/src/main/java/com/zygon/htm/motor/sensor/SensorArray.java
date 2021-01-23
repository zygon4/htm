package com.zygon.htm.motor.sensor;

import com.google.common.collect.Lists;
import com.zygon.htm.core.AbstractScheduledServiceImpl;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

/**
 *
 * @author zygon
 */
public class SensorArray extends AbstractScheduledServiceImpl {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReadLock readLock = lock.readLock();
    private final WriteLock writeLock = lock.writeLock();

    private final Collection<Reading> currentValues = Lists.newArrayList();
    private final Set<Sensor> sensors;
    private Reading currentAggregate = null;

    public SensorArray(Settings settings, Set<Sensor> sensors) {
        super(settings);

        this.sensors = sensors;
    }

    @Override
    protected void doRun() throws Exception {
        this.writeLock.lock();

        try {
            if (!this.currentValues.isEmpty()) {
                // TODO:
//                this.currentAggregate = this.aggregate(this.currentValues);
                this.currentValues.clear();
            }
        } finally {
            this.writeLock.unlock();
        }
    }

//    @Override
//    public String getName() {
//        return getClass().getCanonicalName();
//    }
//
//    @Override
//    public Reading getReading() {
//        this.readLock.lock();
//
//        try {
//            return this.currentAggregate;
//        } finally {
//            this.readLock.unlock();
//        }
//    }
//
//    @Override
//    public void handle(String value) {
//        this.writeLock.lock();
//
//        try {
//            if (this.currentValues != null) {
//                this.currentValues.add(new Reading(value, System.currentTimeMillis()));
//            }
//        } finally {
//            this.writeLock.unlock();
//        }
//    }
}
