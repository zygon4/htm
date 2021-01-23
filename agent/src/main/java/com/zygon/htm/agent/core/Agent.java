package com.zygon.htm.agent.core;

import com.zygon.htm.motor.actuator.Actuator;
import com.zygon.htm.motor.sensor.Sensor;

import java.util.Set;

/**
 * Toy agent interface. No concepts of location or physical robot qualities.
 * Actuators could be anywhere on the planet.
 *
 * @author zygon
 */
public interface Agent {

    void addActuators(Set<Actuator> actuators);

    void addSensors(Set<Sensor> sensors);

    void agentStart();

    void agentStop();
}
