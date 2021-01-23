package com.zygon.htm.motor.actuator.impl;

import com.zygon.htm.motor.actuator.Actuator;

import java.util.Set;

/**
 * A generic movement actuator.
 *
 * @author zygon
 */
public class MovementAcutatorImpl implements Actuator {

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

    private static final Set<String> MOVEMENTS = Set.of(
            LEFT, RIGHT, HALF_LEFT, HALF_RIGHT, HALF_CIRCLE, AHEAD,
            NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST,
            NORTHWEST
    );

    public MovementAcutatorImpl() {

    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public Set<String> getMovementNames() {
        return MOVEMENTS;
    }

    @Override
    public void activate(String movement) {
        System.out.println(getName() + " moving: " + movement);
    }
}
