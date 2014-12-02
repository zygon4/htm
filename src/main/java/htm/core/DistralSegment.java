
package htm.core;

/**
 *
 * @author zygon
 */
public class DistralSegment extends Segment {

    public DistralSegment(int id, InputConductor inputConductor, InhibitionProvider inhibitionProvider) {
        super(id, inputConductor, inhibitionProvider);
    }

    public DistralSegment(int id) {
        super(id);
    }

    /*pkg*/ void attach(DistralSegment otherDistral) {
        // TODO:
    }
}
