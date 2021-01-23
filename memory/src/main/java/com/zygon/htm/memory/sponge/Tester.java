package com.zygon.htm.memory.sponge;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import com.zygon.htm.core.Identifier;
import com.zygon.htm.memory.sponge.substrate.SubstrateNeuron;

/**
 *
 * @author zygon
 */
public class Tester {

    private final ActorRef[] neurons;
    private final ActorSystem actorSystem = ActorSystem.create();

    public Tester() {

        this.neurons = new ActorRef[25];

        int idx = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Identifier id = new Identifier(i, j);
                System.out.println("Making id: " + id);

                Props prop = Props.create(SubstrateNeuron.class, () -> new SubstrateNeuron(id));
                this.neurons[idx++] = actorSystem.actorOf(prop);
            }
        }

    }

//    public AbstractNeuron[] getNeurons() {
//        return this.neurons;
//    }
    public void run() throws Exception {

        for (int i = 0; i < this.neurons.length; i++) {
//            this.neurons[i].startUp();
        }

        try {
            this.neurons[0].tell(Pulse.create(new Direction(new Identifier(5, 5))), this.neurons[0]);
            this.neurons[0].tell(Pulse.create(new Direction(new Identifier(5, 0))), this.neurons[0]);
            this.neurons[0].tell(Pulse.create(new Direction(new Identifier(0, 5))), this.neurons[0]);

            Thread.sleep(5000);
        } finally {
            for (int i = 0; i < this.neurons.length; i++) {
//                this.neurons[i].shutdown();
            }
        }
    }

    public void shutdown() throws InterruptedException {
        actorSystem.terminate().wait(5000);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Tester tester = new Tester();
        try {
            tester.run();
        } finally {
            tester.shutdown();
        }
    }

    private static final class ActorRoot extends AbstractActor {

        final ActorRef root = getContext().actorOf(Props.create(ActorRoot.class));

        public ActorRef create(Props props) {
            return getContext().actorOf(props);
        }

        @Override
        public Receive createReceive() {
            return receiveBuilder().matchAny(x -> getSender().tell(x, getSelf())).build();
        }
    }
}
