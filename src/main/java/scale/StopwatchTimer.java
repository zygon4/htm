
package scale;

/**
 *
 * @author david.charubini
 */
public class StopwatchTimer implements AssociativeNode.Timer {

    private final long maxTime;
    private long startTime = 0;

    public StopwatchTimer(long maxTime) {
        this.maxTime = maxTime;
    }
    
    @Override
    public boolean isExpired() {
        return System.currentTimeMillis() - this.startTime > this.maxTime;
    }

    @Override
    public void clockIn() {
        if (this.isExpired()) {
            throw new IllegalStateException("expired");
        }
    }

    @Override
    public void start() {
        this.startTime = System.currentTimeMillis();
    }
}
