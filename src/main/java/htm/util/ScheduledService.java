
package htm.util;

import com.google.common.util.concurrent.AbstractScheduledService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class ScheduledService extends AbstractScheduledService {

    public static interface Thunk {
        public void run();
    }
    
    private final Thunk runImpl;
    private final Scheduler scheduler;
    
    public ScheduledService(long period, TimeUnit timeUnit, Thunk runImpl) {
        super();
        
        if (runImpl == null) {
            throw new IllegalArgumentException("runImpl cannot be null");
        }
        
        this.runImpl = runImpl;
        this.scheduler = Scheduler.newFixedRateSchedule(0, period, timeUnit);
    }
    
    @Override
    protected void runOneIteration() throws Exception {
        try {
            this.runImpl.run();
        } catch (Exception e) {
            // TODO: log?
            throw e;
        }
    }

    @Override
    protected Scheduler scheduler() {
        return this.scheduler;
    }
}
