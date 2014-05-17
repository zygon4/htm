
package htm.core;

import com.google.common.collect.Lists;
import htm.InputReceiver;
import htm.OutputProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author david.charubini
 */
public class Region {

    private static int SEGMENT_ID = 0;
    
    private static ProximalSegment createProximalSegment(int feedForwardInputCount, Collection<InputReceiver> inputReceivers, Collection<OutputProvider> outputProvider) {
        Collection<Synapse> synapses = Lists.newArrayList();
    
        int SYN_ID = 0;
        
        for (int i = 0; i < feedForwardInputCount; i++) {
            synapses.add(new Synapse(SEGMENT_ID+"_"+SYN_ID++));
        }
        
        SynapseSet synSet = new SynapseSet(synapses);
        
        synSet.addInputReceivers(inputReceivers);
        synSet.addOutputProviders(outputProvider);
        
        return new ProximalSegment(SEGMENT_ID++, synSet, null);
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
    
    public static Region createRegion (int columnCount, int feedForwardSynapseCount, 
            Collection<InputReceiver> inputReceivers, Collection<OutputProvider> outputProvider) {
        
        List<Column> columns = Lists.newArrayList();
        List<Segment> segments = Lists.newArrayList();
        
        for (int i = 0; i < columnCount; i++) {
            ProximalSegment segment = createProximalSegment(feedForwardSynapseCount, inputReceivers, outputProvider);
            columns.add(new Column("col_"+(i+1), segment, null));
            segments.add(segment);
        }
        
        Column[] cols = columns.toArray(new Column[columns.size()]);
        
        for (int j = 0; j < cols.length; j++) {
            Collection<Column> neighbors = getNeighbors(j, cols, DESIRED_LOCAL_ACTIVITY);
            
            LocallyCompetitiveInhibition lci = new LocallyCompetitiveInhibition(neighbors, DESIRED_LOCAL_COMPETITION);
            segments.get(j).setInhibitionProvider(lci);
            
            columns.get(j).setNeighbors(neighbors);
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
            col.start();
        }
    }
    
    /*pkg*/ void uninitialize() {
        for (Column col : this.columns) {
            col.stopAndWait();
        }
    }
}
