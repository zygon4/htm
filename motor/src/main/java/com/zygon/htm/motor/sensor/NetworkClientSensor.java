/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.motor.sensor;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteStreams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class NetworkClientSensor extends SensorImpl {

    private static final Logger logger = LoggerFactory.getLogger(NetworkClientSensor.class);

    private final String remoteHost;
    private final int remotePort;
    private final int dataPacketLen;
    private final String name;

    private Socket socket;
    private InputStream inputStream;

    public NetworkClientSensor(String remoteHost, int remotePort, int dataPacketLen) {
        this.remoteHost = Objects.requireNonNull(remoteHost);
        this.remotePort = remotePort;
        // TBD: this is unbounded
        this.dataPacketLen = dataPacketLen;
        this.name = getClass().getName() + "_" + this.remoteHost + ":" + this.remotePort;
    }

    @Override
    protected void activate() throws SensorLifecycleException {
        Preconditions.checkState(socket == null);
        try {
            socket = new Socket(remoteHost, remotePort);
            inputStream = new BufferedInputStream(socket.getInputStream(), 65535);
        } catch (IOException io) {
            throw new SensorLifecycleException(io);
        }
    }

    @Override
    protected void deactivate() throws SensorLifecycleException {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
            if (inputStream != null) {
                inputStream.close();
                inputStream = null;
            }
        } catch (IOException io) {
            throw new SensorLifecycleException(io);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUnits() {
        // TBD:
        return "bytes";
    }

    @Override
    public Reading getReading() throws SensorReadingException {
        try {
            long start = System.nanoTime();
//            byte[] intData = new byte[4];
//            ByteStreams.readFully(inputStream, intData);
//            int dataLen = ByteBuffer.wrap(intData).order(ByteOrder.LITTLE_ENDIAN).getInt();

            byte[] buffer = new byte[dataPacketLen];

            ByteStreams.readFully(inputStream, buffer);
            long stop = System.nanoTime();

            logger.info("Took {} ms to read data buffer", TimeUnit.NANOSECONDS.toMillis(stop - start));

            return new Reading(buffer, System.currentTimeMillis());
        } catch (IOException io) {
            throw new SensorReadingException(io);
        }
    }
}
