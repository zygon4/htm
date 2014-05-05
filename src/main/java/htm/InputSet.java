
package htm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Consider rename to "ValueSet"
 *
 * @author david.charubini
 */
public class InputSet<T extends Input<?>> {

    private final Map<String, T> inputsById = new HashMap<String, T>();
    private final String[] keys;
    
    public InputSet(Collection<T> inputs) {
        for (T input : inputs) {
            inputsById.put(input.getId(), input);
        }
        
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