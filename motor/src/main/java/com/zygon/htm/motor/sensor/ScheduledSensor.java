package com.zygon.htm.motor.sensor;

import com.zygon.htm.core.AbstractScheduledServiceImpl;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public final class ScheduledSensor extends AbstractScheduledServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledSensor.class);

    private final Sensor sensor;
    private final ReadingHandler<Reading> sensorValueHandler;

    public ScheduledSensor(Settings settings, Sensor sensor, ReadingHandler<Reading> sensorValueHandler) {
        super(settings);

        this.sensor = Objects.requireNonNull(sensor);
        this.sensorValueHandler = Objects.requireNonNull(sensorValueHandler);
    }

    @Override
    protected void doRun() throws Exception {

        switch (sensor.getState()) {
            case activated:
                logger.info("Sensor {} is {}. Reading.", sensor.getName(), sensor.getState());
                Reading reading = this.sensor.getReading();

                if (reading != null) {
//                    byte[] val = reading.getValue();
//                    System.out.println(sensor.getName() + " read value " + Arrays.toString(val));

                    this.sensorValueHandler.handle(sensor.getName(), reading);
                }
                break;
            case activating:
                logger.info("Sensor {} is {}. Doing nothing.", sensor.getName(), sensor.getState());
                // keep waiting
                break;
            case errored:
                logger.info("Sensor {} is {}. Stopping.", sensor.getName(), sensor.getState());
                try {
                    sensor.doStop();
                } catch (SensorLifecycleException se) {
                    // TBD: log
                }
            // FALL THROUGH
            case idle:
                logger.info("Sensor {} is {}. Starting.", sensor.getName(), sensor.getState());
                try {
                    sensor.doStart();
                } catch (SensorLifecycleException se) {
                    // TBD: log
                }
        }
    }

    @Override
    protected void shutDown() throws Exception {
        sensor.doStop();
    }
}
