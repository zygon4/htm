
package com.zygon.htm.core.io.channel.Input;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class RandomInputChannel extends InputChannel<BooleanInputSet> {

    private static Settings createSettings() {
        Settings settings = new Settings();
        
        settings.setPeriod(2);
        settings.setTimeUnit(TimeUnit.SECONDS);
        
        return settings;
    }
    
    private final Random random = new Random();

    public RandomInputChannel() {
        super(createSettings());
    }

    @Override
    public BooleanInputSet getValue() {
        
        List<BooleanInput> inputs = Lists.newArrayList();
        
        for (int i = 0; i < 9; i++) {
            BooleanInput bi = new BooleanInput(String.valueOf(i));
            bi.setValue(this.random.nextBoolean());
            inputs.add(bi);
        }
        
        BooleanInputSet boolInputSet = new BooleanInputSet(inputs);
        
        return boolInputSet;
    }
}
