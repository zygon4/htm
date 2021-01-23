package com.zygon.htm.agent.simple;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zygon.htm.agent.core.Agent;
import com.zygon.htm.core.AbstractScheduledServiceImpl.Settings;
import com.zygon.htm.motor.actuator.Actuator;
import com.zygon.htm.motor.sensor.ScheduledSensor;
import com.zygon.htm.motor.sensor.Sensor;
import info.gridworld.actor.Critter;
import info.gridworld.grid.Location;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class SimpleAgent extends Critter implements Agent {

    private final SenseAggregtor senseAggregtor = new SenseAggregtor(
            Settings.create(15, TimeUnit.SECONDS), "SenseManifold");
    private final List<ScheduledSensor> sensors = Lists.newArrayList();

    private final Set<Actuator> actuators = new LinkedHashSet<>();

    private final String name;
    // TODO: scheduled actuator?
    private LocationAcutator actuator;

    public SimpleAgent(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public void addScheduled(Sensor sensor) {

        ScheduledSensor newSensor = new ScheduledSensor(
                Settings.create(5, TimeUnit.SECONDS),
                sensor,
                senseAggregtor);

        newSensor.startAsync();

        sensors.add(newSensor);
    }

    @Override
    public void addActuators(Set<Actuator> actuators) {
        this.actuators.addAll(actuators);
    }

    @Override
    public void addSensors(Set<Sensor> sensors) {
        sensors.forEach(sensor -> {
            addScheduled(sensor);
        });
    }

    @Override
    public void agentStart() {
        senseAggregtor.startAsync();
    }

    @Override
    public void agentStop() {
        sensors.forEach(ScheduledSensor::stopAsync);
        senseAggregtor.stopAsync();
    }

    public final LocationAcutator getActuator() {
        return actuator;
    }

    @Override
    public ArrayList<Location> getMoveLocations() {
        // TODO: get from sensor
        return getGrid().getValidAdjacentLocations(getLocation());
    }

    public String getName() {
        return name;
    }

    public void setActuator(LocationAcutator locationAcutator) {
        Preconditions.checkState(actuator == null);
        actuator = Objects.requireNonNull(locationAcutator);
    }
}
