package com.zygon.htm.memory.sponge;

import akka.actor.AbstractActor;
import com.zygon.htm.core.Identifier;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

import java.util.Objects;

/**
 *
 * @author zygon
 */
public abstract class AbstractNeuron extends AbstractActor implements INeuron {

    private final Identifier id;

    public AbstractNeuron(Identifier id) {
        super();

        this.id = Objects.requireNonNull(id);
    }

    @Override
    public final Identifier getId() {
        return this.id;
    }

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return super.receive(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Pulse.class, this::processPulse)
                .matchAny(unknown -> unhandled(unknown))
                .build();
    }

//    @Override
//    protected Void doRun() throws InterruptedException, SuspendExecution {
//        register();
//
//        Pulse msg;
//
//        while ((msg = receive()) != null) {
//            processPulse(msg);
//        }
//
//        return null;
//    }
    // blocking
    public final void pulse() throws InterruptedException {
//        this.handle(Pulse.create(new Direction(this.getId())));
    }

    protected abstract void processPulse(Pulse pulse);

    protected void send(Pulse pulse, Identifier id) {

        // TODO: find the actor to send to
        getContext().getParent().tell(pulse, getSelf());

        getContext().findChild(id.getDisplay()).ifPresentOrElse(neuron -> {
            neuron.tell(pulse, neuron);
        },
                () -> {
                    System.out.println("Could not find destination for " + id.getDisplay());
                });

//        getContext().
//    ActorRegi
//        ActorRef<Pulse> actor = ActorRegistry.getActor(id.getDisplay());
//        actor.send(pulse);
    }

//    public void shutdown() throws Exception {
//        this.join(30, TimeUnit.SECONDS);
//    }
//    public void startUp() {
//        spawnThread();
//    }
    @Override
    public String toString() {
        return "{" + this.getId() + "}";
    }
}
