package com.zygon.htm.agent.simple;

import com.google.common.collect.ImmutableMap;
import com.zygon.htm.agent.simple.Sense.Percept;
import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * TODO: finish this up!
 *
 * @author zygon
 */
public class SimpleWorld extends ActorWorld {

    // Meant to overlay
    private static final class Space {

        private final int x;
        private final int y;
        private final boolean isWall;
        private final boolean isAgent;
        private final Map<Sense.Percept, Double> strengthByPercept;

        private Space(int x, int y, boolean isWall, boolean isAgent, Map<Sense.Percept, Double> strengthByPercept) {
            this.x = x;
            this.y = y;
            checkArgument((!isAgent && !isWall) || (isWall && !isAgent) || (!isWall && isAgent));
            this.isWall = isWall;
            this.isAgent = isAgent;
            this.strengthByPercept = ImmutableMap.copyOf(strengthByPercept);
        }

        public Space(int x, int y, boolean isWall, boolean isAgent) {
            this(x, y, isWall, isAgent, Collections.emptyMap());
        }

        public Space(int x, int y) {
            this(x, y, false, false);
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Deprecated
        public boolean isAgent() {
            return isAgent;
        }

        @Deprecated
        public boolean isWall() {
            return isWall;
        }

        @Deprecated
        public Space setIsAgent(boolean isAgent) {
            return new Space(x, y, isWall, isAgent);
        }

        @Deprecated
        public Space setIsWall(boolean isWall) {
            return new Space(x, y, isWall, isAgent);
        }

        public Space setPercept(Percept percept, double strength) {
            Map<Percept, Double> mergedMap = new HashMap<>(strengthByPercept);

            Double val = mergedMap.putIfAbsent(percept, strength);
            if (val != null) {
                // had an old value - merge them (take the higher value)
                double newVal = Math.max(val, strength);

                mergedMap.put(percept, newVal);
            }

            return new Space(x, y, isWall, isAgent, mergedMap);
        }

        protected Map<Percept, Double> getStrengthByPercept() {
            // This is immutable
            return strengthByPercept;
        }
    }

    // TODO: move
    private static Map<Percept, Double> merge(Collection<Map<Percept, Double>> things) {
        Map<Percept, Double> mergedMap = new HashMap<>();

        for (Map<Percept, Double> merge : things) {

            merge.entrySet().forEach(entry -> {
                Percept percept = entry.getKey();
                Double strength = entry.getValue();

                Double val = mergedMap.putIfAbsent(percept, strength);
                if (val != null) {
                    // had an old value - merge them (take the higher value)
                    double newVal = Math.max(val, strength);

                    mergedMap.put(percept, newVal);
                }
            });
        }

        return mergedMap;
    }

//    private final Gaussian gaussian;
    private final Space[][] spaces;

    // From info.gridworld.world.World
    private static final int DEFAULT_ROWS = 10;
    private static final int DEFAULT_COLS = 10;

    public SimpleWorld() {
        super();

        this.spaces = new Space[DEFAULT_COLS][DEFAULT_ROWS];
//        this.gaussian = new Gaussian(spaces.length / 2.0, 4.0); // todo: dynamic sigma
    }

//    public SimpleWorld(int x, int y) {
//        this(create(x, y));
//    }
//    private Space setEcho(Space space) {
//        double nWall = space.getY();
//        double nWallEcho = gaussian.value(nWall) * 10;
//
//        double sWall = spaces.length - space.getY();
//        double sWallEcho = gaussian.value(sWall) * 10;
//
//        double eWall = space.getX();
//        double eWallEcho = gaussian.value(eWall) * 10;
//
//        double wWall = spaces[0].length - space.getX();
//        double wWallEcho = gaussian.value(wWall) * 10;
//
//        double strength = nWallEcho * sWallEcho * eWallEcho * wWallEcho;
//        strength = Math.min(strength, 1.0);
//
//        return space.setPercept(Percept.ECHO, strength);
//    }
//    private Space setPercepts(Space space) {
//        return setEcho(space);
//        // TODO: others
//    }
    public void initialize() {

        Random rand = new Random();
        Percept[] percepts = Percept.values();

        Grid<Actor> grid = getGrid();
        for (int y = 0; y < grid.getNumRows(); y++) {
            for (int x = 0; x < grid.getNumCols(); x++) {

                spaces[x][y] = new Space(x, y);

                Actor actor = grid.get(new Location(x, y));
                if (actor != null) {
                    // Just plop a random percept for testing
                    Percept percept = percepts[rand.nextInt(3)];
                    double strength = rand.nextDouble();

                    spaces[x][y] = spaces[x][y].setPercept(percept, strength);
                }
            }
        }

//        int x = spaces.length;
//        int y = spaces[0].length;
//
//        for (int i = 0; i < x; i++) {
//            for (int j = 0; j < y; j++) {
//                if (i == 0 || j == 0 || i == x - 1 || j == y - 1) {
//                    spaces[i][j] = setPercepts(new Space(i, j).setIsWall(true));
//                } else {
//                    boolean agent = false;
//                    if (!setAgent) {
//                        if (rand.nextDouble() <= (1.0 / ((double) x * y))) {
//                            setAgent = true;
//                            agent = true;
//                        } else {
//                            // just in case we don't random place one
//                            if (i == x - 2 && j == y - 2) {
//                                setAgent = true;
//                                agent = true;
//                            }
//                        }
//                    }
//                    spaces[i][j] = setPercepts(new Space(i, j).setIsAgent(agent));
//                }
//            }
//        }
//
//        for (int i = 0; i < x; i++) {
//            for (int j = 0; j < y; j++) {
//                System.out.printf("%2f ", spaces[i][j].strengthByPercept.get(Percept.ECHO));
//            }
//            System.out.println();
//        }
    }
//
//    private void draw(Space space) {
//        if (space.isWall()) {
//            System.out.print("#");
//        } else {
//            if (space.isAgent()) {
//                System.out.print("@");
//            } else {
//                System.out.print(".");
//            }
//        }
//    }
//
//    private void draw() {
//        for (int i = 0; i < spaces.length; i++) {
//            for (int j = 0; j < spaces[i].length; j++) {
//                draw(spaces[i][j]);
//            }
//            System.out.println();
//        }
//    }
//
//    public static void main(String[] args) {
//        SimpleWorld world = new SimpleWorld(10, 10);
//        world.initialize();
//
//        world.draw();
//    }
//

    public Map<Percept, Double> getStrengthByPercept(Location location) {
        return spaces[location.getCol()][location.getRow()].getStrengthByPercept();
    }

    public Map<Percept, Double> getStrengthByPercept(Collection<Location> locations) {

        Collection<Map<Percept, Double>> merged = new HashSet<>();

        for (Location location : locations) {
            spaces[location.getCol()][location.getRow()].getStrengthByPercept();
        }

        return merge(merged);
    }

    public Optional<SimpleAgent> getSimpleAgent(String name) {
        Grid<Actor> grid = getGrid();
        for (int y = 0; y < grid.getNumRows(); y++) {
            for (int x = 0; x < grid.getNumCols(); x++) {
                Actor actor = grid.get(new Location(x, y));
                if (actor != null) {
                    if (actor instanceof SimpleAgent) {
                        SimpleAgent agent = (SimpleAgent) actor;
                        if (agent.getName().equals(name)) {
                            return Optional.of(agent);
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }

}
