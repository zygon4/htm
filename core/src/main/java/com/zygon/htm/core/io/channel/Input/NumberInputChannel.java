package com.zygon.htm.core.io.channel.Input;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;

import java.util.List;

/**
 *
 * @author zygon
 */
public class NumberInputChannel extends InputChannel<BooleanInputSet> {

    private final Range<Integer> numberRange;

    public NumberInputChannel(int low, int high) {
        super(Settings.create(2));
        this.numberRange = Range.closed(low, high);
    }

    @Override
    public BooleanInputSet getValue() {
        List<BooleanInput> inputs = Lists.newArrayList();

//        for (int i = 0; i < 9; i++) {
//            BooleanInput bi = new BooleanInput(String.valueOf(i));
//            bi.setValue(this.random.nextBoolean());
//            inputs.add(bi);
//        }
        // TODO: map the ints to crude numbers using booleans,
        /*
         * [x][x][x]
         * [x][ ][x]   => 0
         * [x][x][x]
         *
         * [ ][x][ ]
         * [ ][X][ ]   => 1
         * [ ][x][ ]
         *
         * [x][x][ ]
         * [ ][X][x]   => 0
         * [x][x][x]
         */
        BooleanInputSet boolInputSet = new BooleanInputSet(inputs);

        return boolInputSet;
    }
}
