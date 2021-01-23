package com.zygon.htm.memory.core;

import com.zygon.htm.core.Identifier;
import com.zygon.htm.core.message.Message;

/**
 *
 * @author zygon
 */
public class ActivationMessage extends Message {

    public ActivationMessage(Identifier destination, double value, long timestamp) {
        super(Message.Type.ACTIVATION, destination, value, timestamp);
    }

    public ActivationMessage(Identifier destination, double value) {
        this(destination, value, System.currentTimeMillis());
    }
}
