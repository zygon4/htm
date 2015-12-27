
package com.zygon.htm.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class Identifier implements Comparable<Identifier> {

    private final int[] coordinates;
    private final int dimensions;
    private final String display;

    private int hash = -1;

    public Identifier(int ...coords) {
        Preconditions.checkArgument(coords != null);
        Preconditions.checkArgument(coords.length > 0);

        this.coordinates = coords;
        this.dimensions = this.coordinates.length;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.coordinates.length; i++) {
            sb.append(this.coordinates[i]);
            if (i < this.coordinates.length - 1) {
                sb.append("_");
            }
        }

        this.display = sb.toString();
    }

    // I don't like exposing this
    public final int getDimensions() {
        return dimensions;
    }

    /* Distances by dimension */
    public int[] getDistances(Identifier o) {

        Preconditions.checkArgument(o.dimensions == this.dimensions);

        int[] distancesByDimension = new int[this.dimensions];

        for (int i = 0; i < this.coordinates.length; i++) {
            distancesByDimension[i] = this.coordinates[i] - o.coordinates[i];
        }

        return distancesByDimension;
    }


    /**
     * Returns an array of angles between these two Identifiers.
     * @param o
     * @return
     */
    public double getDirection(Identifier o) {
        Preconditions.checkArgument(o.dimensions == this.dimensions);

        // right now this will support 2D - 3D to come
        Preconditions.checkArgument(o.dimensions == 2);

        double[] pointLocationDeltas = new double[this.dimensions];

        for (int i = 0; i < this.dimensions; i++) {
            pointLocationDeltas[i] = o.coordinates[i] - this.coordinates[i];
        }

        return Math.toDegrees(Math.atan2(pointLocationDeltas[1], pointLocationDeltas[0]));
    }

    public static void main(String[] args) {
        System.out.println (Math.toDegrees(Math.atan2(1,1)));
        System.out.println (Math.toDegrees(Math.atan2(-1,1)));
        System.out.println (Math.toDegrees(Math.atan2(1,-1)));
        System.out.println (Math.toDegrees(Math.atan2(-1,-1)));

        System.out.println("--------------------------");

        System.out.println (Math.toDegrees(Math.atan2(1,0)));
        System.out.println (Math.toDegrees(Math.atan2(-1,0)));
        System.out.println (Math.toDegrees(Math.atan2(0,1)));
        System.out.println (Math.toDegrees(Math.atan2(0,-1)));

        System.out.println("--------------------------");

        double[] angles = {
            new Identifier(0, 0).getDirection(new Identifier(-1, 1)),
            new Identifier(0, 0).getDirection(new Identifier(0, 1)),
            new Identifier(0, 0).getDirection(new Identifier(1, 1)),

            new Identifier(0, 0).getDirection(new Identifier(-1, 0)),
            new Identifier(0, 0).getDirection(new Identifier(0, 0)),
            new Identifier(0, 0).getDirection(new Identifier(1, 0)),

            new Identifier(0, 0).getDirection(new Identifier(-1, -1)),
            new Identifier(0, 0).getDirection(new Identifier(0, -1)),
            new Identifier(0, 0).getDirection(new Identifier(1, -1)),
        };

        for (double angle : angles) {
            System.out.println(angle);
        }

        System.out.println("--------------------------");
        double[] angles2 = {
            new Identifier(1, 1).getDirection(new Identifier(-1, 1)),
            new Identifier(0, 0).getDirection(new Identifier(0, 1)),
            new Identifier(0, 0).getDirection(new Identifier(1, 1)),

            new Identifier(0, 0).getDirection(new Identifier(-1, 0)),
            new Identifier(0, 0).getDirection(new Identifier(0, 0)),
            new Identifier(0, 0).getDirection(new Identifier(1, 0)),

            new Identifier(0, 0).getDirection(new Identifier(-1, -1)),
            new Identifier(0, 0).getDirection(new Identifier(0, -1)),
            new Identifier(0, 0).getDirection(new Identifier(1, -1)),
        };

        for (double angle : angles2) {
            System.out.println(angle);
        }
    }

    /*
    (defn- dis [x y]
        (math/sqrt (reduce + (map (fn [a b] (math/expt (- a b) 2)) x y))))
    */
    public double getDistance(Identifier o) {

        Preconditions.checkArgument(o.dimensions == this.dimensions);

        double total = 0.0;

        for (int i = 0; i < this.coordinates.length; i++) {
            total += Math.pow(this.coordinates[i] - o.coordinates[i], 2);
        }

        return Math.sqrt(total);
    }

    // This does not seem to fully work for 3d
    // TBD: direction option
    public Collection<Identifier> getNeighbors (int radius) {
        Collection<Identifier> neighbors = Lists.newArrayList();

        // For each dimension
        for (int i = 0; i < this.dimensions; i++) {
            // Take the value AT that dimension and find the neighbors based on
            // the radius.

            int dimValue = this.coordinates[i];

            long min = Math.max(dimValue - Math.round(((double)radius / 2.0)), 0);
            long max = Math.min(dimValue + Math.round((double)(radius / 2.0)), Integer.MAX_VALUE);

            // squirrely casting
            for (int j = (int)min; j <= max; j++) {
                if (j != dimValue) {
                    int[] coords = Arrays.copyOf(this.coordinates, this.coordinates.length);
                    coords[i] = j;
                    neighbors.add(new Identifier(coords));
                }
            }
        }

        return neighbors;
    }

    @Override
    public int compareTo(Identifier o) {
        return o.hashCode() > this.hashCode() ? -1 : (o.hashCode() < this.hashCode() ? 1 : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Identifier)) {
            return false;
        }

        return this.hashCode() == ((Identifier)obj).hashCode();
    }

    @Override
    public final int hashCode() {

        if (this.hash == -1) {
            this.hash = Arrays.hashCode(this.coordinates);
        }

        return this.hash;
    }

    public String getDisplay() {
        return this.display;
    }

    @Override
    public String toString() {
        return this.getDisplay();
    }
}
