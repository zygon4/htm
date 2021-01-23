package com.zygon.htm.agent.core;

import com.google.common.collect.Sets;
import com.zygon.htm.motor.actuator.Actuator;
import com.zygon.htm.motor.sensor.Sensor;

import java.util.Set;

/**
 *
 * @author zygon
 */
public class AgentImpl implements Agent {

    private final Set<Actuator> actuators = Sets.newLinkedHashSet();
    private final Set<Sensor> sensors = Sets.newLinkedHashSet();

    public AgentImpl(Set<Actuator> actuators, Set<Sensor> sensors) {
        this.actuators.addAll(actuators);
        this.sensors.addAll(sensors);
    }

    @Override
    public void agentStart() {
        // nothing by default
    }

    @Override
    public void agentStop() {
        // nothing by default
    }

    @Override
    public void addActuators(Set<Actuator> actuators) {
        this.actuators.addAll(actuators);
    }

    @Override
    public void addSensors(Set<Sensor> sensors) {
        this.sensors.addAll(sensors);
    }
}
