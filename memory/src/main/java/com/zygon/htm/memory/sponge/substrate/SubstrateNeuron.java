package com.zygon.htm.memory.sponge.substrate;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.zygon.htm.core.Identifier;
import com.zygon.htm.memory.sponge.AbstractNeuron;
import com.zygon.htm.memory.sponge.Direction;
import com.zygon.htm.memory.sponge.Pulse;

import java.util.Map;

import org.apache.commons.math3.stat.Frequency;

/**
 *
 * @author zygon
 */
public final class SubstrateNeuron extends AbstractNeuron {

    // This can *never* hold individual originating ids - it would explode
    // at scale. This just wants to hold basic direction and strength of
    // the pulses.
    private static final class Manifold {

        // 1's based
        private final Map<Integer, Frequency> frequenciesByDimension = Maps.newLinkedHashMap();

        public void addPoint(int dimension, int value, int strength) {
            Preconditions.checkArgument(dimension > 0);
            Preconditions.checkArgument(strength > 0);

            Frequency dimFrequencies = this.frequenciesByDimension.get(dimension);

            if (dimFrequencies == null) {
                dimFrequencies = new Frequency();
                this.frequenciesByDimension.put(dimension, dimFrequencies);
            }

            dimFrequencies.addValue(value);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Integer, Frequency> entry : this.frequenciesByDimension.entrySet()) {
                sb.append(entry.getKey()).append("|").append(entry.getValue());
            }
            return sb.toString();
        }
    }

    public SubstrateNeuron(Identifier id) {
        super(id);
    }

    @Override
    protected void processPulse(Pulse pulse) {

        // Add pulse to frequency matrix direction->value
        // Value is calculated by the direction the pulse hits us offset by
        // the angle.
        // 1) Calc the angle between a Direction and a Identifier
        // @ThisDon'tMakeSense
        // 2) Offset that value based on it's absolute distance/angle from the what???
        //    it doesn't make sense to calc it based on.. what? our id? How is a
        //    pulse entering us from an angle other than N/S/E/W hindered?
        // All "priority" channels must be relative to other, known channels.
        // 3) Add value to a matrix
//        double pulseAngle = pulse.getOrigin().getAbsoluteAngle(this.getId());
        double pulseAngle = this.getId().getDirection(pulse.getOrigin().getOrigin());

        System.out.println(this.getId() + " Processing pulse: " + pulse);

        System.out.println(this.getId() + " pulse angle: " + pulseAngle);

        send(pulse, new Identifier(0, 4));
    }

    // pathways are in respect to a direction - calculate all for entropy?
    private double calculatePathway(Direction dir) {
        return 1.0;
    }
}
