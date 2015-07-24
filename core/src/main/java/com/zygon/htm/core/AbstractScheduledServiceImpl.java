
package com.zygon.htm.core;

import com.google.common.util.concurrent.AbstractScheduledService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public abstract class AbstractScheduledServiceImpl extends AbstractScheduledService {

    public static final class Settings {

        public static Settings create(long period, TimeUnit timeUnit) {
            Settings s = new Settings();
            s.setPeriod(period);
            s.setTimeUnit(timeUnit);
            return s;
        }

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

    protected AbstractScheduledServiceImpl(Settings settings) {
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
        try {
            this.doRun();
        } catch (Exception e) {
            // TODO: log?
            e.printStackTrace();
        }
    }

    @Override
    protected final Scheduler scheduler() {
        return this.scheduler;
    }
}
