package com.zygon.htm.memory;

import com.zygon.htm.core.AbstractScheduledServiceImpl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class MemorySystemImpl extends AbstractScheduledServiceImpl implements MemorySystem {

    private final Map<String, ReadableByteChannel> inputsByName = new TreeMap<>();
    private final Map<String, ByteBuffer> byteBuffersByName = new HashMap<>();

    public MemorySystemImpl() {
        super(Settings.create(10, TimeUnit.SECONDS));
    }

    @Override
    public void start() {
        startAsync();
    }

    @Override
    public void stop() {
        stopAsync();
    }

    @Override
    public Queue<Object> inputs() {
        return null;
    }

    @Override
    public Queue<Object> actions() {
        return null;
    }

    @Override
    public Queue<Object> input() {
        return null;
    }

    @Override
    public Object getAction(double entropy) {
        return null;
    }

    @Override
    public void add(String inputName, ReadableByteChannel inputChannel) {
        inputsByName.put(inputName, inputChannel);
        byteBuffersByName.put(inputName, ByteBuffer.allocate(1024 * 1024));
    }

    @Override
    public ReadableByteChannel getOutput() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doRun() throws Exception {
        // TODO:

        inputsByName.entrySet().parallelStream().forEach((isEntry) -> {
            String key = isEntry.getKey();
            ReadableByteChannel value = isEntry.getValue();

            try {
                ByteBuffer bb = byteBuffersByName.get(key);

                // Clear if we've read all the data
                if (bb.remaining() == 0) {
                    bb.clear();
                    if (value.read(bb) == -1) {
                        // TODO: another option?
                        System.out.println("Channel " + key + " is closed.");
                    }
                } else {
                    byte data = bb.get();
                    System.out.println("Channel " + key + " returned data " + data);

                    // TBD: merge the channels data together in realtime
                }

            } catch (IOException io) {
                // TBD
                io.printStackTrace();
            }
        });

        // Read input channels, convert to SDR
        // Pipe SDR output to internal memory representation (the ongoing experiments)
        // Take current output from internal memory representation and send to output channel
    }
}
