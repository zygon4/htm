
package htm.core;

import htm.Input;
import java.util.Collection;

/**
 *
 * @author david.charubini
 */
public class Cell extends Input<Boolean> {

    public static enum State {
        ACTIVE_FEEDFORWARD,
        ACTIVE_PREDICT,
        NOT_ACTIVE;
    }

    private final Collection<DistralSegment> distralDendrites;
    
    public Cell(String id, Collection<DistralSegment> distralDendrites) {
        super(id);
        this.distralDendrites = distralDendrites;
    }
    
    private volatile State state = State.NOT_ACTIVE;
    
    @Override
    public boolean isActive() {
        return this.state == State.ACTIVE_FEEDFORWARD || this.state == State.ACTIVE_PREDICT;
    }

    @Override
    public String getDisplayString() {
        return "pretty cell: " + this.state.name();
    }
}
