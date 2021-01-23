
package com.zygon.htm.memory.entropy;

import java.util.List;

/**
 *
 * @author zygon
 */
@FunctionalInterface
public interface Action {
    List<Step> doAction();
}
