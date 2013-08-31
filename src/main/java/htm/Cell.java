
package htm;

import htm.input.Input;

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

    public Cell(String id) {
        super(id);
    }
    
    // TBD: state lock
    private State state = State.NOT_ACTIVE;
    
    @Override
    public boolean isActive() {
        return this.state == State.ACTIVE_FEEDFORWARD || this.state == State.ACTIVE_PREDICT;
    }

    @Override
    public String getDisplayString() {
        return "pretty cell: " + this.state.name();
    }
}
