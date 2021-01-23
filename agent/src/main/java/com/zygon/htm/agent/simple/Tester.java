package com.zygon.htm.agent.simple;

import info.gridworld.actor.Bug;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Location;
import org.apache.commons.math3.util.Pair;

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author zygon
 */
public class Tester {

    private static class SimpleWorldSensor extends SenseSensor {

        private final String agentName;

        public SimpleWorldSensor(String sensorName, SimpleWorld simpleWorld, String agentName) {
            super(sensorName, simpleWorld);

            this.agentName = agentName;
        }

        @Override
        protected Collection<Sense> getSenses() {
            SimpleWorld world = getSimpleWorld();
            Optional<SimpleAgent> agentOpt = world.getSimpleAgent(agentName);

            Collection<Sense> senses = new HashSet<>();

            if (agentOpt.isPresent()) {
                SimpleAgent agent = agentOpt.get();
                Location agentLocation = agent.getLocation();

                for (Location location : agent.getMoveLocations()) {
                    int directionToward = agentLocation.getDirectionToward(location);
                    Direction direction = Direction.valueOf(directionToward);
                    Map<Sense.Percept, Double> strengthByPercept = world.getStrengthByPercept(location);

                    strengthByPercept.entrySet().forEach(entry -> {
                        senses.add(new Sense(entry.getKey(), Pair.create(direction, entry.getValue())));
                    });

                }
            }

            return senses;
        }
    }

    /**
     * @param args the command line arguments
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        // Create a world
        SimpleWorld world = new SimpleWorld();

        world.add(new Location(7, 8), new Rock());
        world.add(new Location(3, 3), new Rock());
        world.add(new Location(2, 8), new Rock(Color.BLUE));
        world.add(new Location(5, 5), new Rock(Color.PINK));
        world.add(new Location(1, 5), new Rock(Color.RED));
        world.add(new Location(7, 2), new Rock(Color.YELLOW));
        world.add(new Location(4, 4), new Bug());
//            world.add(new Location(5, 8), new ChameleonCritter());

        // Add our agent to the world
        SimpleAgent agent = new SimpleAgent("joe");
        world.add(new Location(8, 8), agent);

        world.initialize();

        // Create an output actuator
        LocationAcutator actuator = new LocationAcutator(agent);

        // Set the actuator on the agent
        agent.setActuator(actuator);

        // Create and add sensors to the agent
        agent.addScheduled(new SimpleWorldSensor("WorldSensor", world, agent.getName()));
//        agent.addScheduled(new Sensor() {
//
//            private final Random rand = new Random();
//
//            @Override
//            public String getName() {
//                return "ECHO sensor";
//            }
//
//            @Override
//            public String getUnits() {
//                return "percept";
//            }
//
//            @Override
//            public State getState() {
//                return State.activated;
//            }
//
//            @Override
//            public Reading getReading() {
//                // Why isn't this just (shuffle [0, 45, 90, -45, -90]) ???
//                List<Direction> directions = new ArrayList<>();
//                directions.addAll(List.of(Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH));
////                directions.addAll(List.of(0, 45, 90, -45, -90));
//                Collections.shuffle(directions);
//
//                Pair<Direction, Double> percept = Pair.create(directions.get(0), rand.nextDouble());
//                return new Reading(new Sense(Sense.Percept.ECHO, percept).getDisplayString().getBytes(), System.currentTimeMillis());
//            }
//
//            @Override
//            public void doStart() throws SensorLifecycleException {
//                // nothing to do?
//            }
//
//            @Override
//            public void doStop() throws SensorLifecycleException {
//                // nothing to do?
//            }
//        });
//
//        agent.add(
//                new Sensor<Sense>() {
//
//                    @Override
//                    public String getName() {
//                        return "DRAFT sensor";
//                    }
//
//                    @Override
//                    public Reading<Sense> getReading() {
//                        Map<Sense.Percept, Pair<Integer, Double>> percepts = Maps.newHashMap();
//                        Random rand = new Random();
//                        List<Integer> directions = Lists.newArrayList(0, 45, 90, -45, -90);
//                        Collections.shuffle(directions);
//                        percepts.put(Sense.Percept.DRAFT, Pair.create(directions.get(0), rand.nextDouble()));
//                        return new Reading<>(new Sense("DRAFT sensor", percepts), System.currentTimeMillis());
//                    }
//                }
//        );
//
//        agent.add(
//                new Sensor<Sense>() {
//
//                    @Override
//                    public String getName() {
//                        return "SMELL sensor";
//                    }
//
//                    @Override
//                    public Reading<Sense> getReading() {
//                        Map<Sense.Percept, Pair<Integer, Double>> percepts = Maps.newHashMap();
//                        Random rand = new Random();
//                        List<Integer> directions = Lists.newArrayList(0, 45, 90, -45, -90);
//                        Collections.shuffle(directions);
//                        percepts.put(Sense.Percept.SMELL, Pair.create(directions.get(0), rand.nextDouble()));
//                        return new Reading<>(new Sense("SMELL sensor", percepts), System.currentTimeMillis());
//                    }
//                }
//        );
        agent.agentStart();

//        try {
        world.show();

//            Thread.sleep(TimeUnit.SECONDS.toMillis(30));
//        } finally {
//            agent.agentStop();
//        }
    }
}
