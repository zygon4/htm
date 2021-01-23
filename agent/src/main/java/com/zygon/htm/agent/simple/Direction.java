package com.zygon.htm.agent.simple;

/**
 *
 * @author zygon
 */
public enum Direction {

    NORTH(0),
    NORTHEAST(45),
    EAST(90),
    SOUTHEAST(135),
    SOUTH(180),
    SOUTHWEST(225),
    WEST(270),
    NORTHWEST(315);

    private final int angle;

    private Direction(int angle) {
        this.angle = angle;
    }

//    public static final int LEFT = -90;
//    public static final int RIGHT = 90;
//    public static final int HALF_LEFT = -45;
//    public static final int HALF_RIGHT = 45;
//    public static final int FULL_CIRCLE = 360;
//    public static final int HALF_CIRCLE = 180;
//    public static final int AHEAD = 0;
//    public static final int NORTH = 0;
//    public static final int NORTHEAST = 45;
//    public static final int EAST = 90;
//    public static final int SOUTHEAST = 135;
//    public static final int SOUTH = 180;
//    public static final int SOUTHWEST = 225;
//    public static final int WEST = 270;
//    public static final int NORTHWEST = 315;
//
    public int getAngle() {
        return angle;
    }

    // I've never used this exception before, just want a checked exception of some kind
    public static Direction valueOf(int fromAngle) throws EnumConstantNotPresentException {
        for (Direction dir : Direction.values()) {
            if (dir.getAngle() == fromAngle) {
                return dir;
            }
        }

        throw new EnumConstantNotPresentException(Direction.class, String.valueOf(fromAngle));
    }
}
