/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.sdr.motor.sensor;

import com.google.common.io.BaseEncoding;
import com.zygon.htm.motor.sensor.Reading;
import com.zygon.htm.motor.sensor.Sensor;
import com.zygon.htm.motor.sensor.SensorImpl;
import com.zygon.htm.motor.sensor.SensorLifecycleException;
import com.zygon.htm.motor.sensor.SensorReadingException;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * SensorAggregator is mono-type readings.. so not good enough
 *
 * @author zygon
 */
public class SDRSensor extends SensorImpl {

    private final Collection<Sensor> sensors;

    public SDRSensor(Collection<Sensor> sensors) {
        // TBD: make sure these are aggregators?

        this.sensors = Objects.requireNonNull(sensors);
    }

    @Override
    public String getUnits() {
        return "sdr";
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Reading getReading() throws SensorReadingException {
        Map<String, List<Sensor>> sensorsByReadingUnit = sensors.parallelStream()
                .collect(Collectors.groupingBy(Sensor::getUnits));

        // TODO: take the sensors by unit and turn them into an SDR
        sensorsByReadingUnit.entrySet().parallelStream()
                .forEach(entry -> {
                    System.out.println("=================================");
                    System.out.println("Sensor type: " + entry.getKey());
                    for (Sensor sensor : entry.getValue()) {
                        System.out.println("Name: " + sensor.getName());

                        try {
                            Reading reading = sensor.getReading();
                            System.out.println("ts: " + reading.getTimestamp());
                            System.out.println("data: " + BaseEncoding.base16().lowerCase().encode(reading.getValue()));
                        } catch (SensorReadingException sre) {
                            // naughty
                            sre.printStackTrace();
                            // TODO log
                        }
                    }
                });

    }

    @Override
    protected void activate() throws SensorLifecycleException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void deactivate() throws SensorLifecycleException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
