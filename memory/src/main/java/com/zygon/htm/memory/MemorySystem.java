/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.memory;

import java.nio.channels.ReadableByteChannel;
import java.util.Queue;

/**
 * First try at having a memory interface. Needs to be able to accept input and
 * predict output.
 *
 *
 * Question: Does all sensor input need to be merged with action output? Similar
 * to an RNN?
 *
 */
public interface MemorySystem {

    // Lifecycle based?
    void start();

    void stop();

    // Plus something simple like this?
    Queue<Object> inputs();

    Queue<Object> actions();
    // not very robust but certainly simple

    // Or, more specific, which deals with some internal concepts:
    Queue<Object> input();

    Object getAction(double entropy);

    // Or super generic byte transfer??
    void add(String inputName, ReadableByteChannel inputChannel);

    ReadableByteChannel getOutput();
}
