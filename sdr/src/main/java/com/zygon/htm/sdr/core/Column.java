
package com.zygon.htm.sdr.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AbstractScheduledService;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author david.charubini
 */
public class Column extends AbstractScheduledService {

    private static final Scheduler SCHEDULER = Scheduler.newFixedDelaySchedule(0, 500, TimeUnit.MILLISECONDS);
    
    private final String id;
    private final ProximalSegment proximalDendrite;
    private final Collection<Cell> cells = Lists.newArrayList();
    
    private Collection<Column> neighbors;

    public Column (String id, ProximalSegment proximalDendrite, Collection<Cell> cells) {
        super();
        Preconditions.checkNotNull(cells);
        this.id = id;
        this.proximalDendrite = proximalDendrite;
        this.cells.addAll(cells);
    }
    
    public Column(String id, ProximalSegment proximalDendrite) {
        this (id, proximalDendrite, Collections.EMPTY_LIST);
    }

    public void add (Cell cell) {
        this.cells.add(cell);
    }

    /*pkg*/ Collection<Cell> getCells() {
        return this.cells;
    }
    
    public String getDisplayString() {
        // Checking for active could be intensive. Consider caching
        // or sparingly using this method.
        return "Column [" + this.id + "] active [" + this.isActive() + "]";
    }
    
    /*pkg*/ Collection<Segment> getNeighboringSegments() {
        Collection<Segment> localSegments = Lists.newArrayList();
        
        for (Column localColumn : this.neighbors) {
            localSegments.add(localColumn.proximalDendrite);
        }
        
        return localSegments;
    }

    public Segment getProximalDendrite() {
        return this.proximalDendrite;
    }
    
    public boolean isActive() {
        return this.isFeedForwardActive() || this.isHorizontalActive();
    }

    private boolean isFeedForwardActive() {
        return this.proximalDendrite.isActive();
    }
    
    private boolean isHorizontalActive() {
        
        for (Cell cell : this.cells) {
            if (cell.isActive()) {
                return true;
            }
        }
        
        return false;
    }
    
    protected void doSpatialPooling() {
        // 1) Tell the lower level synapses to compute their overlap score
        this.proximalDendrite.process();
        
        // 3) learn
        this.proximalDendrite.learn(this.isActive(), getNeighboringSegments());
    }
    
    protected void doTemporalPooling() {
        for (Cell cell : this.cells) {
//            cell.process();
//            cell.learn();
        }
    }
    
    @Override
    protected void runOneIteration() throws Exception {
        this.doSpatialPooling();
        this.doTemporalPooling();
    }

    @Override
    protected Scheduler scheduler() {
        return SCHEDULER;
    }
    
    /*pkg*/ void setNeighbors (Collection<Column> neighbors) {
        if (neighbors == null) {
            throw new IllegalArgumentException();
        }
        
        // This set is not expected to happen more than once at init time.
        if (this.neighbors != null) {
            throw new IllegalStateException();
        }
        
        this.neighbors = neighbors;
    }
}
