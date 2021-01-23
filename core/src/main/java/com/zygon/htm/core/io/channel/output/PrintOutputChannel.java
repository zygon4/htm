package com.zygon.htm.core.io.channel.output;

import com.zygon.htm.core.io.Input;
import com.zygon.htm.core.io.InputSet;
import com.zygon.htm.core.io.OutputProvider;

import java.util.Collection;

/**
 *
 * @author zygon
 */
public class PrintOutputChannel extends OutputChannel<InputSet<?>> {

    public PrintOutputChannel() {
        super(Settings.create(5));
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
