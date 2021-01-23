package com.zygon.htm.memory.sponge;

import com.google.common.util.concurrent.AbstractExecutionThreadService;
import com.zygon.htm.core.Identifier;

import java.util.function.Consumer;

/**
 * Class to drive the memory processing as a serialized process vs parallelized.
 * This is primarily for initial development and testing. Parallel processing
 * can and should be easy in this case.
 *
 * @author zygon
 */
public class MemoryDriver extends AbstractExecutionThreadService implements Consumer<Pulse> {

    private static final int SPONGE_DIMENSION_SIZE = 10;

    private static void print(AbstractNeuron[][] sponge) {
        for (int i = SPONGE_DIMENSION_SIZE - 1; i >= 0; i--) {
            for (int j = 0; j < SPONGE_DIMENSION_SIZE; j++) {
                System.out.print(sponge[j][i] + " ");
            }
            System.out.println();
        }
    }

    private final AbstractNeuron[][] sponge;

    public MemoryDriver() {
        this.sponge = new AbstractNeuron[SPONGE_DIMENSION_SIZE][SPONGE_DIMENSION_SIZE];

        for (int i = 0; i < SPONGE_DIMENSION_SIZE; i++) {
            for (int j = 0; j < SPONGE_DIMENSION_SIZE; j++) {

                this.sponge[i][j] = new SpongeNeuron(new Identifier(i, j), this);
            }
        }
    }

    @Override
    public void accept(Pulse pulse) {
        System.out.println(pulse);
    }

    @Override
    protected void run() throws Exception {
        while (this.isRunning()) {

//            Collection<Pulse> surrounding = this.sponge[0][0].calculatePulses();
            this.sponge[1][1].send(Pulse.create(new Direction(this.sponge[0][0].getId())), this.sponge[1][1].getId());

//            this.sponge[1][1].handle(Pulse.create(new Direction(this.sponge[0][0].getId())));
//            for (Identifier neighbor : this.sponge[0][0].getId().getNeighbors(1)) {
//                System.out.println(neighbor);
//            }
//
//            for (Identifier neighbor : new Identifier(1,1,1).getNeighbors(1)) {
//                System.out.println(neighbor);
//            }
            Thread.sleep(1000);
        }
    }

    @Override
    protected void startUp() throws Exception {
        super.startUp();

        for (int i = 0; i < SPONGE_DIMENSION_SIZE; i++) {
            for (int j = 0; j < SPONGE_DIMENSION_SIZE; j++) {
//                sponge[i][j].startUp();
            }
        }
    }

    @Override
    protected void shutDown() throws Exception {
        super.shutDown();

        for (int i = 0; i < SPONGE_DIMENSION_SIZE; i++) {
            for (int j = 0; j < SPONGE_DIMENSION_SIZE; j++) {
//                sponge[i][j].shutdown();
            }
        }
    }

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        MemoryDriver driver = new MemoryDriver();

        MemoryDriver.print(driver.sponge);

        driver.startAsync();

        try {
            Thread.sleep(10000);
        } finally {
            driver.stopAsync();
        }
    }
}
