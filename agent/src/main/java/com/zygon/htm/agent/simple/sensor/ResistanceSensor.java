package com.zygon.htm.agent.simple.sensor;

import com.zygon.htm.agent.simple.Sense;
import com.zygon.htm.agent.simple.SenseSensor;
import com.zygon.htm.agent.simple.SimpleWorld;

import java.util.Collection;

/**
 * General sensor to represent the local movement resistance. Think wall = 100%,
 * nothing = 0%, some kind of obstacle = 50%.
 *
 * @author zygon
 */
public class ResistanceSensor extends SenseSensor {

    public ResistanceSensor(SimpleWorld simpleWorld) {
        super("resistence", simpleWorld);
    }

    @Override
    protected Collection<Sense> getSenses() {
//        return new Sense(name, )
        // don't have resistence yet
        return null;
    }
}
