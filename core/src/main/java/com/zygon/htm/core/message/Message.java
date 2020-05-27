package com.zygon.htm.core.message;

import com.zygon.htm.core.Identifier;

import java.util.Date;

/**
 *
 * @author zygon
 */
public class Message {

    // This is a bit of an anti-pattern, would rather rely on subtyping OR
    // use a builder and make this final
    public static enum Type {
        ACTIVATION,
        PREDICTION,
    }

    private final Type type;
    private final Identifier destination;
    private final double value;
    private final long timestamp;

    private final String display;

    public Message(Type type, Identifier destination, double value, long timestamp) {
        this.type = type;
        this.value = value;
        this.destination = destination;
        this.timestamp = timestamp;

        this.display = this.type.name() + "," + "[dest:" + this.destination + "],"
                + this.value + "," + new Date(this.timestamp);
    }

    public final Identifier getDestination() {
        return destination;
    }

    public String getDisplay() {
        return display;
    }

    public final long getTimestamp() {
        return timestamp;
    }

    public final Type getType() {
        return type;
    }

    public final double getValue() {
        return value;
    }

    public Message setDestination(Identifier dest) {
        return new Message(this.type, dest, this.value, this.timestamp);
    }

    @Override
    public String toString() {
        return this.display;
    }
}
