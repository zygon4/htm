
package htm;

import htm.input.InputSet;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author david.charubini
 */
public class InputProvider extends ArrayBlockingQueue<InputSet> {
    private static final long serialVersionUID = 1L;

    public InputProvider() {
        super(1000);
    }

    
}
