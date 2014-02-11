package htm.pooling.spatial;

import htm.Column;
import htm.Region;
import htm.input.InputSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author david.charubini
 */
public class SpatialPooler {

    private static final int DESIRED_LOCAL_ACTIVITY = 10;
    
    // This is just quick/dirty neighborhood calculation
    private static Collection<Column> getNeighbors(int idx, Column[] columns, int radius) {
        Collection<Column> neighbors = new ArrayList<Column>();
        
        int min = Math.max(idx - (radius / 2), 0);
        int max = Math.min(idx + (radius / 2), columns.length);
        
        for (int i = min; i < max; i++) {
            if (i != idx) {
                neighbors.add(columns[i]);
            }
        }
        
        return neighbors;
    }
    
    public void process(Region region, InputSet inputSet) {
        
        region.setInput(inputSet);
        
        // 1) process input
        for (Column column : region.getColumns()) {
            column.process();
        }
        
        // 2) inhibit
        Column[] columns = region.getColumns().toArray(new Column[region.getColumns().size()]);
        
        for (int i = 0; i < columns.length; i++) {
            
            Column column = columns[i];
            
            Collection<Column> neighbors = getNeighbors(i, columns, DESIRED_LOCAL_ACTIVITY);
            
            if (column.isActive()) {
                boolean suppress = !column.isActivityGreaterThanLocal(neighbors);
                column.setSuppressed(suppress);
            }
            
            System.out.printf("[%s]", column.isActive() ? "+" : " ");
            
            // 3) learn
            column.learn(neighbors);
        }

        System.out.println("\n----------------------------------\n");
    }
}
