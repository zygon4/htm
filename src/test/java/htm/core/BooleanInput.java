
package htm.core;

import htm.Input;

/**
 *
 * @author zygon
 */
/*pkg*/ class BooleanInput extends Input<Boolean> {

    public BooleanInput(String id) {
        super(id);
    }

    @Override
    public String getDisplayString() {
        return this.getClass().getName()+"_"+this.getId();
    }

    @Override
    public boolean isActive() {
        return this.getValue() == Boolean.TRUE;
    }
}
