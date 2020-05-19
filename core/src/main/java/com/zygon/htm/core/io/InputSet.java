package com.zygon.htm.core.io;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Consider rename to "ValueSet"
 *
 * @author david.charubini
 * @param <T>
 */
public class InputSet<T extends Input<?>> {

    private final Map<String, T> inputsById;
    private final String[] keys;

    public InputSet(Collection<T> inputs) {
        this.inputsById = inputs.stream()
                .collect(Collectors.toMap(i -> i.getId(), i -> i));

        // There's probably a better way of storing this info, or not duplicating it
        this.keys = this.inputsById.keySet().toArray(new String[this.inputsById.size()]);
    }

    public void getInputs(Collection<T> col, int count) {
        Random rand = new Random(System.currentTimeMillis());

        for (int i = 0; i < count; i++) {
            int idx = rand.nextInt(this.inputsById.size());
            col.add(this.inputsById.get(this.keys[idx]));
        }
    }

    public Collection<T> getInputs() {
        return this.inputsById.values();
    }

    public Input getById(String id) {
        return this.inputsById.get(id);
    }
}
