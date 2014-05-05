
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
        
        for (OutputProvider val : outputProviders) {
            
            System.out.print("(");
            System.out.print(val.getId());
            
            if (val.isOutputActive()) {
                Input value = val.getOutput();
                if (value != null && value.isActive()) {
                    System.out.print("[ X ]");
                } else {
                    System.out.print("[ _ ]");
                }
            } else {
                System.out.print("[   ]");
            }
            
            System.out.print(") ");
        }
        
        System.out.println();
        
        return null;
    }
}
