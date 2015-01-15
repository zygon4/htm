package com.zygon.htm.sdr;

/**
 *
 * @author davec
 */
public interface InputReceiver<T extends Input<?>> {
    public String getId();
    public void send (T input);
}
