
package htm;

import htm.input.Input;
import htm.input.InputSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author david.charubini
 */
public class Region {

    private final Collection<Column> columns;
    private final Map<String, List<InputReceiver>> inputReceiversById = new HashMap<String, List<InputReceiver>>();

    public Region(Collection<Column> columns) {
        this.columns = Collections.unmodifiableCollection(columns);
        
        Collection<InputReceiver> inputReceivers = new ArrayList<InputReceiver>();
        
        for (Column col : this.columns) {
            inputReceivers.addAll(col.getFeedForwardInputReceivers());
        }
        
        for (InputReceiver inputReceiver : inputReceivers) {
            List<InputReceiver> recList = this.inputReceiversById.get(inputReceiver.getId());
            if (recList == null) {
                recList = new ArrayList<InputReceiver>();
                this.inputReceiversById.put(inputReceiver.getId(), recList);
            }
            recList.add(inputReceiver);
        }
    }

    public void getConnectedInputs(Collection<Input<?>> connectedInputs) {
        for (Column col : this.columns) {
            if (!col.isSuppressed()) {
                col.getConnectedInputs(connectedInputs);
            }
        }
    }
    
    public Collection<Column> getColumns() {
        return this.columns;
    }

    // soo this is pretty gnarly.. but i think it works
    public void setInput(InputSet inputSet) {
        Iterator<String> iterator = this.inputReceiversById.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Input input = inputSet.getById(key);
            if (input != null) {
                List<InputReceiver> receivers = this.inputReceiversById.get(key);
                if (receivers != null && !receivers.isEmpty()) {
                    for (InputReceiver receiver : receivers) {
                        receiver.send(input);
                    }
                }
            }
        }
    }
}
