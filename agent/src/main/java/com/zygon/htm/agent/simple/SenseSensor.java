package com.zygon.htm.agent.simple;

import com.zygon.htm.motor.sensor.Reading;
import com.zygon.htm.motor.sensor.SensorImpl;
import com.zygon.htm.motor.sensor.SensorLifecycleException;
import com.zygon.htm.motor.sensor.SensorReadingException;

import java.util.Collection;

/**
 *
 * @author zygon
 */
public abstract class SenseSensor extends SensorImpl {

    private final String sensorName;
    private final SimpleWorld simpleWorld;

    public SenseSensor(String sensorName, SimpleWorld simpleWorld) {
        this.sensorName = sensorName;
        this.simpleWorld = simpleWorld;
    }

    @Override
    protected void activate() throws SensorLifecycleException {
        // Do nothing?
    }

    @Override
    protected void deactivate() throws SensorLifecycleException {
        // Do nothing?
    }

    @Override
    public final String getName() {
        return sensorName;
    }

    protected abstract Collection<Sense> getSenses();

    protected final SimpleWorld getSimpleWorld() {
        return simpleWorld;
    }

    @Override
    public final Reading getReading() throws SensorReadingException {
        Collection<Sense> sense = getSenses();

        sense.forEach(s -> {
            System.out.println(s.getDisplayString());
        });

        // TODO: translate
        return null;
    }

    @Override
    public final String getUnits() {
        // TBD: something better?
        return Sense.Percept.class.getSimpleName();
    }
}
