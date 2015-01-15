
package com.zygon.htm.sdr.core;

import com.zygon.htm.sdr.channel.Input.InputChannel;
import com.zygon.htm.sdr.channel.output.OutputChannel;

/**
 *
 * @author zygon
 */
public interface IHTM {
    
    public void initialize();
    
    public void register(InputChannel input);
    
    public void register(OutputChannel input);
    
    public void uninitialize();
}
