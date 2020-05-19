package com.zygon.htm.core.io.channel.Input;

import com.zygon.htm.core.io.Input;

/**
 *
 * @author zygon
 */
public class BooleanInput extends Input<Boolean> {

    public BooleanInput(String id) {
        super(id);
    }

    @Override
    public String getDisplayString() {
        return this.getClass().getName() + "_" + this.getId();
    }

    @Override
    public boolean isActive() {
        return this.getValue().equals(Boolean.TRUE);
    }
}
