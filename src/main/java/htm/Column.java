
package htm;

import htm.input.Input;
import htm.input.InputSet;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author david.charubini
 */
public class Column extends Input<Boolean> {

    private final DescriptiveStatistics activeDutyCycle = new DescriptiveStatistics(1000);
    private final Segment proximalDendrite;
    private final Collection<Segment> distralDendrites;
    
    private boolean suppressed = false;

    public Column(String id, Segment proximalDendrite, Collection<Segment> distralDendrites) {
        super(id);
        this.proximalDendrite = proximalDendrite;
        this.distralDendrites = distralDendrites;
    }
    
    public void getConnectedInputs(Collection<Input<?>> connectedInputs) {
        this.proximalDendrite.getConnectedInputs(connectedInputs);
    }

    @Override
    public String getDisplayString() {
        return "Column ["+ this.getId() + "] active [" + this.isActive() + "]";
    }
    
    public Collection<InputReceiver> getFeedForwardInputReceivers() {
        return this.proximalDendrite.getInputReceivers();
    }
    
    private Collection<Segment> getSegments(Collection<Column> columns) {
        Collection<Segment> localSegments = new ArrayList<Segment>();
        
        for (Column localColumn : columns) {
            localSegments.add(localColumn.proximalDendrite);
        }
        
        return localSegments;
    }
    
    @Override
    public boolean isActive() {
        return !this.suppressed && this.isFeedForwardActive();
//        return !this.suppressed && (this.isFeedForwardActive() || this.isHorizontalActive());
    }

    private boolean isFeedForwardActive() {
        return this.proximalDendrite.isActive();
    }
    
    private boolean isHorizontalActive() {
        
        for (Segment segment : this.distralDendrites) {
            if (segment.isActive()) {
                return true;
            }
        }
        
        return false;
    }
    
    public void learn(Collection<Column> neighbors) {
        this.proximalDendrite.learn(this.isActive(), this.activeDutyCycle.getMean(), getSegments(neighbors));
    }
    
    public void process() {
        this.proximalDendrite.process();
        this.activeDutyCycle.addValue(this.isFeedForwardActive() ? 1.0 : 0.0);
    }
    
    public void setSuppressed(boolean suppressed) {
        this.suppressed = suppressed;
    }

    void setInput(InputSet input) {
        this.proximalDendrite.setInput(input);
    }
    
    public boolean isActivityGreaterThanLocal(Collection<Column> neighbors) {
        Collection<Segment> localSegments = getSegments(neighbors);
        return this.proximalDendrite.isOverlapGreaterThanLocal(localSegments);
    }
}
