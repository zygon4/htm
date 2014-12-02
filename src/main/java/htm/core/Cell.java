
package htm.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import htm.Input;
import java.util.Map;

/**
 *
 * @author david.charubini
 */
public class Cell extends Input<Boolean> {

    private static int D_SEGMENT_ID = 2000;
    
    private final int depth;
    private final Map<Integer,DistralSegment> distralDendrites = Maps.newHashMap();
    
    public Cell(String id, int depth) {
        super(id);
        
        Preconditions.checkArgument(depth >= 0);
        this.depth = depth;
    }
    
    /*pkg*/ void attach(Cell other) {
        System.out.println("attaching " + other + " to " + this + " at depth " + depth);
        
        // the local distral for going to that remote depth
        DistralSegment localDistral = this.distralDendrites.get(other.depth);
        
        if (localDistral == null) {
            localDistral = new DistralSegment(D_SEGMENT_ID++);
            this.distralDendrites.put(other.depth, localDistral);
        }
        
        // The remote distral for going to this depth
        DistralSegment otherDistral = other.distralDendrites.get(this.depth);
        
        if (otherDistral == null) {
            otherDistral = new DistralSegment(D_SEGMENT_ID++);
            other.distralDendrites.put(this.depth, otherDistral);
        }
        
        localDistral.attach(otherDistral);
    }
    
    /*pkg*/ Map<Integer,DistralSegment> getDistralDendrites() {
        return this.distralDendrites;
    }
    
    @Override
    public boolean isActive() {
        
        for (DistralSegment distral : this.distralDendrites.values()) {
            if (distral.isActive()) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public String getDisplayString() {
        // Checking for active could be intensive. Consider caching
        // or sparingly using this method.
        return "cell Id: " + this.getId() + ", depth: "+ this.depth + ", active: " + this.isActive();
    }
}
