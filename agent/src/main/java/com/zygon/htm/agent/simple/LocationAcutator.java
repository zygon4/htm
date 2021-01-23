package com.zygon.htm.agent.simple;

import info.gridworld.grid.Location;

import com.google.common.collect.Maps;
import com.zygon.htm.motor.actuator.Actuator;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author zygon
 */
public class LocationAcutator implements Actuator {

    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";
    public static final String HALF_LEFT = "HALF_LEFT";
    public static final String HALF_RIGHT = "HALF_RIGHT";
    public static final String FULL_CIRCLE = "FULL_CIRCLE";
    public static final String HALF_CIRCLE = "HALF_CIRCLE";
    public static final String AHEAD = "AHEAD";
    public static final String NORTH = "NORTH";
    public static final String NORTHEAST = "NORTHEAST";
    public static final String EAST = "EAST";
    public static final String SOUTHEAST = "SOUTHEAST";
    public static final String SOUTH = "SOUTH";
    public static final String SOUTHWEST = "SOUTHWEST";
    public static final String WEST = "WEST";
    public static final String NORTHWEST = "NORTHWEST";

    private static final Map<String, Integer> DIRECTIONS = Maps.newLinkedHashMap();

    private final SimpleAgent agent;

    public LocationAcutator(SimpleAgent agent) {
        this.agent = Objects.requireNonNull(agent);
        DIRECTIONS.put(LEFT, Location.LEFT);
        DIRECTIONS.put(RIGHT, Location.RIGHT);
        DIRECTIONS.put(HALF_LEFT, Location.HALF_LEFT);
        DIRECTIONS.put(HALF_RIGHT, Location.HALF_RIGHT);
        DIRECTIONS.put(FULL_CIRCLE, Location.FULL_CIRCLE);
        DIRECTIONS.put(HALF_CIRCLE, Location.HALF_CIRCLE);
        DIRECTIONS.put(AHEAD, Location.AHEAD);
        DIRECTIONS.put(NORTH, Location.NORTH);
        DIRECTIONS.put(NORTHEAST, Location.NORTHEAST);
        DIRECTIONS.put(EAST, Location.EAST);
        DIRECTIONS.put(SOUTHEAST, Location.SOUTHEAST);
        DIRECTIONS.put(SOUTH, Location.SOUTH);
        DIRECTIONS.put(SOUTHWEST, Location.SOUTHWEST);
        DIRECTIONS.put(WEST, Location.WEST);
        DIRECTIONS.put(NORTHWEST, Location.NORTHWEST);
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Set<String> getMovementNames() {
        return DIRECTIONS.keySet();
    }

    @Override
    public void activate(String movement) {
        // no idea .. this is broken
        Location loc = new Location(DIRECTIONS.get(movement), 0);
        agent.moveTo(loc);
    }
}
