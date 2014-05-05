
package htm.core;

import htm.channel.Input.InputChannel;
import htm.channel.output.OutputChannel;

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
