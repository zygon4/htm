
package com.zygon.htm.sdr.core;

import com.google.common.collect.Lists;
import com.zygon.htm.sdr.InputReceiver;
import com.zygon.htm.sdr.OutputProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author david.charubini
 */
public class Region {

    private static int P_SEGMENT_ID = 0;
    
    private static ProximalSegment createProximalSegment(int feedForwardInputCount, 
            Collection<InputReceiver> inputReceivers, Collection<OutputProvider> outputProvider) {
        Collection<Synapse> synapses = Lists.newArrayList();
    
        int SYN_ID = 0;
        
        for (int i = 0; i < feedForwardInputCount; i++) {
            synapses.add(new Synapse(P_SEGMENT_ID+"_"+SYN_ID++));
        }
        
        SynapseSet synSet = new SynapseSet(synapses);
        
        synSet.addInputReceivers(inputReceivers);
        synSet.addOutputProviders(outputProvider);
        
        return new ProximalSegment(P_SEGMENT_ID++, synSet, null);
    }
    
    private static Collection<Column> getNeighbors(int idx, Column[] columns, int radius) {
        Collection<Column> neighbors = Lists.newArrayList();
        
        int min = Math.max(idx - (radius / 2), 0);
        int max = Math.min(idx + (radius / 2), columns.length);
        
        for (int i = min; i < max; i++) {
            if (i != idx) {
                neighbors.add(columns[i]);
            }
        }
        
        return neighbors;
    }
    
    private static final int DESIRED_LOCAL_ACTIVITY = 10;
    private static final double DESIRED_LOCAL_COMPETITION = 85.0;
    
    private static final int CELL_COUNT = 2; // TODO: pass in
    private static int CELL_ID = 1000;
    
    public static Region createRegion (int columnCount, int feedForwardSynapseCount, 
            Collection<InputReceiver> inputReceivers, Collection<OutputProvider> outputProvider) {
        
        List<Column> columns = Lists.newArrayList();
        List<Segment> segments = Lists.newArrayList();
        
        for (int i = 0; i < columnCount; i++) {
            ProximalSegment proximalSegment = createProximalSegment(feedForwardSynapseCount, inputReceivers, outputProvider);
            columns.add(new Column("col_"+(i+1), proximalSegment));
            segments.add(proximalSegment);
        }
        
        Column[] cols = columns.toArray(new Column[columns.size()]);
        
        for (int j = 0; j < cols.length; j++) {
            Collection<Column> neighbors = getNeighbors(j, cols, DESIRED_LOCAL_ACTIVITY);
            
            LocallyCompetitiveInhibition lci = new LocallyCompetitiveInhibition(neighbors, DESIRED_LOCAL_COMPETITION);
            segments.get(j).setInhibitionProvider(lci);
            
            columns.get(j).setNeighbors(neighbors);
        }
        
        
        List<List<Cell>> cellsByDepth = Lists.newArrayList();
        
        int C_ID = 0;
        
        // For each column, we want to add cells
        for (Column col : columns) {
            
            // For each cell, we want to add a number of distrals
            for (int j = 0; j < CELL_COUNT; j++) {
                
                Cell c = new Cell (CELL_ID+"_"+C_ID++, j);
                col.add(c);
                CELL_ID++;
                
                // Store the cells by depth for later use. We still need
                // to assign distrals to them.
                List<Cell> cells = null;
                
                if (cellsByDepth.size() <= j) {
                    cells = Lists.newArrayList();
                    cellsByDepth.add(cells);
                } else {
                    cells = cellsByDepth.get(j);
                }
                
                cells.add(c);
            }
        }
        
        // All the cells at each depth need to be joined to all cells on each 
        // depth. Essentially, fully connected.  However, for now, lets just
        // connect each cell to each other cell on its own depth. So, each
        // cell will only have a single distral.
        for (int i = 0; i < cellsByDepth.size(); i++) {
            
            List<Cell> cellsAtDepth = cellsByDepth.get(i);
            
            for (int j = 0; j < cellsAtDepth.size(); j++) {
                
                Cell cell = cellsAtDepth.get(j);
                
                for (int k = 0; k < cellsAtDepth.size(); k++) {
                    
                    Cell other = cellsAtDepth.get(k);
                    
                    if (cell != other) {
                        cell.attach(other);
                    }
                }
            }
        }
        
        return new Region(columns);
    }
    
    private final Collection<Column> columns;

    public Region(Collection<Column> columns) {
        this.columns = Collections.unmodifiableCollection(columns);
    }
    
    public Collection<Column> getColumns() {
        return this.columns;
    }
    
    /*pkg*/ void initialize() {
        for (Column col : this.columns) {
            col.startAsync();
        }
    }
    
    /*pkg*/ void uninitialize() {
        for (Column col : this.columns) {
            col.stopAsync();
        }
    }
}
