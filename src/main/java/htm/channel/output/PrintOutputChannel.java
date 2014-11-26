
package htm.channel.output;

import htm.Input;
import htm.InputSet;
import htm.OutputProvider;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class PrintOutputChannel extends OutputChannel<InputSet<?>> {

    private static Settings createSettings() {
        Settings settings = new Settings();
        
        settings.setPeriod(5);
        settings.setTimeUnit(TimeUnit.SECONDS);
        
        return settings;
    }
    
    public PrintOutputChannel() {
        super(createSettings());
    }

    @Override
    protected void doWrite(InputSet<?> values) {
//        Collection<?> inputs = values.getInputs();
//        for (Input<?> value : values.getInputs()) {
//            
//        }
    }

    @Override
    protected InputSet getOutput(Collection<OutputProvider> outputProviders) {
        
        StringBuilder sb = new StringBuilder();
        
        for (OutputProvider val : outputProviders) {
            
            sb.append("(");
            sb.append(val);
            sb.append(" ");
            
            if (val.isOutputActive()) {
                Input value = val.getOutput();
                if (value != null && value.isActive()) {
                    sb.append("[ X ]");
                } else {
                    sb.append("[ _ ]");
                }
            } else {
                sb.append("[   ]");
            }
            
            sb.append(")\n");
        }
        
        System.out.println(sb.toString());
        
        return null;
    }
}
