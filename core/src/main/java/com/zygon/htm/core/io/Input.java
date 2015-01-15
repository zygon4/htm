package com.zygon.htm.core.io;

/**
 * TBD: rename to be something like "value"?
 *
 * @author davec
 * @param <T>
 */
public abstract class Input<T> {
    
    private final String id;
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
