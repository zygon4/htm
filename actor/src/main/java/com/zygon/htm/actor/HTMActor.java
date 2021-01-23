/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.actor;

import com.zygon.htm.core.Identifier;

/**
 *
 * maybe transform this into a message-based proxy?
 */
public interface HTMActor {
    Identifier getIdentifier();
    void tell(Object msg); // maybe???
}
