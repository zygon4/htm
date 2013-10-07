
package scale;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author david.charubini
 */
public class AssociativeNode<T, K> {

    public static interface Timer {
        public boolean isExpired();
        public void clockIn();
        public void start();
    }
    
    public static interface Processor<T, K> {
        /**
         * Returns null if the information is not relevant, a value of type K
         * otherwise.
         * @param t
         * @return null if the information is not relevant, a value of type K
         * otherwise.
         */
        public K process (Collection<T> t);
    }
    
    public static interface Listener<K> {
        public void notifyActive(K k);
    }
    
    private final Timer timer;
    private final Processor<T, K> processor;
    private Listener<K> listener;

    private final List<T> currentValues = new ArrayList<T>();
    
    public AssociativeNode(Timer timer, Processor<T, K> processor, Listener<K> listener) {
        this.timer = timer;
        this.processor = processor;
        this.listener = listener;
    }
    
    public AssociativeNode(Timer timer, Processor<T, K> processor) {
        this(timer, processor, null);
    }

    public synchronized void process (T t) {
        if (this.timer.isExpired()) {
            this.currentValues.clear();
            this.timer.start();
        }
        
        this.currentValues.add(t);
        
        this.timer.clockIn();
        
        K result = this.processor.process(this.currentValues);
        if (result != null) {
            if (this.listener != null) {
                this.listener.notifyActive(result);
            }
        }
    }

    public final void setListener(Listener<K> listener) {
        this.listener = listener;
    }    
}
