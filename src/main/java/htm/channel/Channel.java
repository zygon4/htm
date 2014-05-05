
package htm.channel;

import com.google.common.util.concurrent.AbstractScheduledService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public abstract class Channel extends AbstractScheduledService {
    
    public static final class Settings {
        
        private long period = 0;
        private TimeUnit timeUnit = TimeUnit.SECONDS;

        public long getPeriod() {
            return period;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public void setPeriod(long period) {
            this.period = period;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }
    }
    
    private final Scheduler scheduler;
    
    protected Channel(Settings settings) {
        super();
        
        if (settings == null) {
            // defaults
            settings = new Settings();
        }
        
        this.scheduler = Scheduler.newFixedRateSchedule(0, settings.getPeriod(), settings.getTimeUnit());
    }
    
    protected abstract void doRun() throws Exception;
    
    @Override
    protected final void runOneIteration() throws Exception {
        this.doRun();
        // TODO: log?
    }
    
    @Override
    protected final Scheduler scheduler() {
        return this.scheduler;
    }
}
