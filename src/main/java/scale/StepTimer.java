
package scale;

/**
 *
 * @author david.charubini
 */
public class StepTimer implements AssociativeNode.Timer {

    private final int maxSteps;
    private int currentStepCount = 0;

    public StepTimer(int maxSteps) {
        this.maxSteps = maxSteps;
    }
    
    @Override
    public boolean isExpired() {
        return this.currentStepCount == this.maxSteps;
    }

    @Override
    public void clockIn() {
        if (this.isExpired()) {
            throw new IllegalStateException("expired");
        }
        this.currentStepCount ++;
    }

    @Override
    public void start() {
        System.out.println("Restarting");
        this.currentStepCount = 0;
    }
}
