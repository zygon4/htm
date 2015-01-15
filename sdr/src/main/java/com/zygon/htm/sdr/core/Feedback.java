
package com.zygon.htm.sdr.core;

import com.google.common.collect.Lists;
import java.util.List;

/**
 *
 * @author zygon
 */
public class Feedback {

    public static enum Type {
        ACTIVE_DUTY_CYCLE,
        BOOST,
        POSITIVE,
        NEGATIVE
    }
    
    private final List<Type> types = Lists.newArrayList();
    
    public void add(Type feedback) {
        this.types.add(feedback);
    }

    public List<Type> getTypes() {
        return this.types;
    }
}
