
package htm.core;

import com.google.common.collect.Lists;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.apache.commons.math3.stat.StatUtils;

/**
 *
 * @author zygon
 */
public class LocallyCompetitiveInhibition implements InhibitionProvider {

    /**
     * Returns the highest index representing the value which is within the
     * given percentile.
     * 
     * @param values
     * @param percentile
     * @return the highest index representing the value which is within the
     * given percentile.
     */
    private static int getMinPctIdx(double[] values, double percentile) {
        
        Arrays.sort(values);
        
        double pct = StatUtils.percentile(values, percentile);
        
        for (int i = 0; i < values.length; i++) {
            if (pct <= values[i]) {
                return i;
            }
        }
        
        return 0;
    }
    
    private final Collection<Column> neighbors;
    private final double minPctActivity;

    public LocallyCompetitiveInhibition(Collection<Column> neighbors, double minPctActivity) {
        this.neighbors = neighbors;
        
        if (minPctActivity < 1.0) {
            throw new IllegalArgumentException();
        }
        
        this.minPctActivity = minPctActivity;
    }
    
    @Override
    public double getValue() {
        
        List<Double> neighboringValues = Lists.newArrayList();
        
        for (Column col : this.neighbors) {
            neighboringValues.add(col.getProximalDendrite().getInputConductor().getValue());
        }
        
        double[] values = new double[neighboringValues.size()];
        
        for (int i = 0; i < neighboringValues.size(); i++) {
            values[i] = neighboringValues.get(i);
        }
        
        int minpctIdx = getMinPctIdx(values, this.minPctActivity);
        
        return values[minpctIdx];
    }
}
