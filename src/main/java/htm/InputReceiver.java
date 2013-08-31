package htm;

import htm.input.Input;

/**
 *
 * @author davec
 */
public interface InputReceiver {

    public String getId();
    public void send (Input<?> input);
}
