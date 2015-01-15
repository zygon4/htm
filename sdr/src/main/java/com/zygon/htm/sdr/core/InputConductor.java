
package com.zygon.htm.sdr.core;

/**
 *
 * @author zygon
 */
public interface InputConductor {
    public void provideFeedback(Feedback feedback);
    public double getValue();
}
