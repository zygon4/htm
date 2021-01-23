package com.zygon.htm.core;

import com.google.common.util.concurrent.AbstractScheduledService;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public abstract class AbstractScheduledServiceImpl extends AbstractScheduledService {

    public static final class Settings {

        private final long period;
        private final TimeUnit timeUnit;

        private Settings(long period, TimeUnit timeUnit) {
            this.period = period;
            this.timeUnit = timeUnit;
        }

        public static Settings create(long period, TimeUnit timeUnit) {
            return new Settings(period, timeUnit);
        }

        /**
         * Returns settings using TimeUnit.SECONDS.
         *
         * @param period
         * @return
         */
        public static Settings create(long period) {
            return create(period, TimeUnit.SECONDS);
        }

        public long getPeriod() {
            return period;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }
    }

    private final Scheduler scheduler;

    protected AbstractScheduledServiceImpl(Settings settings) {
        super();

        if (settings == null) {
            settings = Settings.create(0);
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
