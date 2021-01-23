package com.zygon.htm.core.io.channel.Input;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Random;

/**
 *
 * @author zygon
 */
public class RandomInputChannel extends InputChannel<BooleanInputSet> {

    private final Random random = new Random();

    public RandomInputChannel() {
        super(Settings.create(2));
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
