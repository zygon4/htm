/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.memory.sponge.impl2;

import com.zygon.htm.core.Identifier;

/**
 *
 * @author zygon
 */
public class Neuron {
//    private final Manifold manifold = new Manifold();

    private final Identifier identifier;

    public Neuron(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public double getImpedience() {
        return 0.0;
    }
}
