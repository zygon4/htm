
package htm.input;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author david.charubini
 */
public class InputSet {

    private final Map<String, Input> inputsById;
    private final String[] keys;
    
    public InputSet(Map<String, Input> inputsById) {
        this.inputsById = Collections.unmodifiableMap(inputsById);
        this.keys = this.inputsById.keySet().toArray(new String[this.inputsById.size()]);
    }
    
    public synchronized void addInputs(Collection<Input> col, int count) {
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