package com.zygon.htm.agent.simple;

import com.zygon.htm.motor.sensor.Reading;
import com.zygon.htm.motor.sensor.SensorAggregator;

import java.util.Collection;

/**
 *
 * @author zygon
 */
public class SenseAggregtor extends SensorAggregator {

    public SenseAggregtor(Settings settings, String name) {
        super(settings, name);
    }

    @Override
    protected Reading aggregate(Collection<Reading> values) {

        throw new UnsupportedOperationException();
//        Reading aggregate = new Reading(values.stream()
//                .map(Reading::getValue)
//                .map()
//                .reduce(Sense::add).get(),
//                System.currentTimeMillis()
//        );
//
//        System.out.println(name + " value " + aggregate.getValue());
//
//        return aggregate;
    }

    @Override
    public String getUnits() {
        return Sense.class.getSimpleName();
    }
}
