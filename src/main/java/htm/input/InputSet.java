
package htm.input;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author david.charubini
 */
public class InputSet {

    private final Map<String, Input<?>> inputsById = new HashMap<String, Input<?>>();
    private final String[] keys;
    
    public InputSet(Collection<Input<?>> inputs) {
        for (Input<?> input : inputs) {
            inputsById.put(input.getId(), input);
        }
        
        this.keys = this.inputsById.keySet().toArray(new String[this.inputsById.size()]);
    }
    
    public void addInputs(Collection<Input> col, int count) {
        Random rand = new Random(System.currentTimeMillis());
        
        for (int i = 0; i < count; i++) {
            int idx = rand.nextInt(this.inputsById.size());
            col.add(this.inputsById.get(this.keys[idx]));
        }
    }
    
    public Input getById(String id) {
        return this.inputsById.get(id);
    }
}