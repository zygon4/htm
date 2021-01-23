/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.actor;

import akka.actor.UntypedActor;

import com.google.common.base.Preconditions;
import com.zygon.htm.core.Identifier;

import java.util.Objects;

/**
 *
 * @author zygon
 */
class HTMActorImpl extends UntypedActor {

    private Identifier identifier;
    
    @Override
    public void onReceive(Object o) throws Throwable {
        System.out.println(getSelf().path() + " received: " + o);
        
        if (o instanceof Identifier) {
            setIdentifier((Identifier)o);
        } else {
            unhandled(o);
        }
    }
    
    void setIdentifier(Identifier identifier) {
        Preconditions.checkState(this.identifier == null);
        this.identifier = Objects.requireNonNull(identifier);
    }
}
