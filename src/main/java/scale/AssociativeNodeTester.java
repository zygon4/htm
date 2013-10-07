
package scale;

import java.util.Collection;

/**
 *
 * @author davec
 */
public class AssociativeNodeTester {

    private final AssociativeNode.Timer timer = new StepTimer(10);
    private final AssociativeNode.Listener<Boolean> listener = new AssociativeNode.Listener<Boolean>() {

        @Override
        public void notifyActive(Boolean active) {
            System.out.println("woot active!");
        }
    };
    
    private final AssociativeNode.Processor<Integer, Boolean> processor = new AssociativeNode.Processor<Integer, Boolean>() {

        @Override
        public Boolean process(Collection<Integer> t) {
            double sum = 0;
            for (int a : t) {
                sum += a;
            }
            
            double avg = !t.isEmpty() ? sum / (double)t.size() : 0.0;
            
            return avg >= 50;
        }
    };
    
    private final AssociativeNode<Integer, Boolean> timeNode = new AssociativeNode<Integer, Boolean>(timer, processor, listener);
    
    public static void main(String[] args) {
        AssociativeNodeTester tester = new AssociativeNodeTester();
        
        int j = 48;
        for (int i = 0; i < 100; i++) {
            tester.timeNode.process(j);
            if (i % 10 == 0) {
                j = 48;
            } else {
                j++;
            }
        }
        
    }
}
