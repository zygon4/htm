
package com.zygon.htm.memory.entropy;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Super vague!
 *
 * @author zygon
 */
public class Step {

    private final Action action;
    private final List<Step> steps;
    private final double localEntropy;

    public Step(Action action, List<Step> steps) {
        this.action = action;
        this.steps = ImmutableList.copyOf(steps);
        this.localEntropy = Math.pow(this.steps.size(), 2);
    }

    public Step step() {
        return new Step(action, action.doAction());
    }

    public double getEntropy() {
        return getEntropy(1);
    }

    private double getEntropy(int distance) {
        return localEntropy + steps.stream()
                .mapToDouble(step -> step.getEntropy(distance + 1))
                .reduce(0, (l, r) -> Double.sum(l, (r / distance)));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        List<Step> steps = Lists.newArrayList(
//                new Step(Lists.newArrayList(new Step(Collections.emptyList()))),
//                new Step(Collections.emptyList()));
//
//        Step root = new Step(steps);
//
//        System.out.println(root.getEntropy());
//
//        List<Double> doubles = Lists.newArrayList(1.0, 2.0);
//
//
//        System.out.println(
//                doubles.stream()
//                    .reduce(0.0, (l, r) -> Double.sum(l, (Math.log(r)))));
//
////        for (double i = 1; i < 100; i++) {
////            System.out.println(1.0 / i);
////        }

    }
}
