
package com.zygon.mmesh.message;

import com.zygon.htm.core.Identifier;

/**
 *
 * @author zygon
 */
public interface Destination {
    public MessageQueue getQueue();
    public Identifier getIdentifier();
}
