package htm.input;

/**
 *
 * @author davec
 */
public abstract class Input<T> {
    
    private String id;
    private T value;

    public Input(String id) {
        this.id = id;
    }    
    
    public abstract String getDisplayString();
    
    public final String getId() {
        return this.id;
    }

    public final T getValue() {
        return this.value;
    }
    
    public abstract boolean isActive();
    
    public final void setValue (T value) {
        this.value = value;
    }
}
