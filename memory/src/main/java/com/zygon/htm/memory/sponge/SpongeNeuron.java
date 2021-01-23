package com.zygon.htm.memory.sponge;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.zygon.htm.core.Identifier;
import org.apache.commons.math3.stat.Frequency;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 *
 * @author zygon
 */
public class SpongeNeuron extends AbstractNeuron {

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

    private final Manifold manifold = new Manifold();
    private final Consumer<Pulse> pulseHandler;

    public SpongeNeuron(Identifier id, Consumer<Pulse> pulseHandler) {
        super(id);
        this.pulseHandler = Preconditions.checkNotNull(pulseHandler);
    }

    @Override
    protected void processPulse(Pulse pulse) {
        // Pulse starts here
        if (pulse.getOrigin().getOrigin().equals(this.getId())) {

            Map<Identifier, Pulse> pulses = this.calculateOutwardPulses(pulse);

            pulses.forEach((id, p) -> {
                try {
                    send(p, id);
                } catch (Exception ex) {
                    // TBD
                    ex.printStackTrace();
                }
            });

        } else {
            // pulse started elsewhere

            // (Original) keep track of it via dimensions
            int[] distancesByDimension = this.getId().getDistances(pulse.getOrigin().getOrigin());

            for (int i = 0; i < distancesByDimension.length; i++) {
                this.manifold.addPoint(i + 1, distancesByDimension[i], pulse.getStrength());
            }
        }
    }

    @Override
    public String toString() {
        return "{" + this.getId() + ":" + this.manifold + "}";
    }

    private Map<Identifier, Pulse> calculateOutwardPulses(Pulse source) {

        Map<Identifier, Pulse> pulses = Maps.newHashMap();

        // Started here - send pulses to everyone
        if (source.getOrigin().getOrigin().equals(this.getId())) {

            Collection<Identifier> neighbors = this.getId().getNeighbors(1);

            for (Identifier neighbor : neighbors) {
                pulses.put(neighbor, source.createPropagationPulse());

                // TBD: this needs a direction associated with it to be sendable
            }

        } else {
            // TODO: the forwarded pulses MUST respect the source direction.

        }

        return pulses;
    }
}
