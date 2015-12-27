
package com.zygon.htm.motor.sensor;

import com.google.common.base.Preconditions;
import com.zygon.htm.core.AbstractScheduledServiceImpl;

/**
 *
 * @author zygon
 * @param <T>
 */
public final class ScheduledMonitor<T> extends AbstractScheduledServiceImpl {

    private final Sensor<T> sensor;
    private final ValueHandler<T> monitorValueHandler;

    public ScheduledMonitor(Settings settings, Sensor<T> sensor, ValueHandler<T> sensorValueHandler) {
        super(settings);
        Preconditions.checkNotNull(sensor);
        Preconditions.checkNotNull(sensorValueHandler);

        this.sensor = sensor;
        this.monitorValueHandler = sensorValueHandler;
    }

    @Override
    protected void doRun() throws Exception {
        Reading<T> reading = this.sensor.getReading();

        if (reading != null) {
            T val = reading.getValue();
            this.monitorValueHandler.handle(val);
        }
    }
}
