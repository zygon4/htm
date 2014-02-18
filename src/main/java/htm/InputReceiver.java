package htm;

/**
 *
 * @author davec
 */
public interface InputReceiver {

    public String getId();
    public void send (Input<?> input);
}
