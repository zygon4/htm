/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zygon.htm.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.SelectionPathElement;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Sets;
import com.zygon.htm.core.Identifier;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author zygon
 */
public class HTMActorController {

    private static final String WILDCARD_ACTOR = "*";

    @Deprecated
    private final int ACTOR_COUNT = 10;

    private final ActorSystem actorSystem;

    public HTMActorController(String name, Set<Identifier> identifiers) {
        this.actorSystem = ActorSystem.create(Objects.requireNonNull(name));

        // flat level of actors, could be hierarchical in the future
        for (Identifier identifier : identifiers) {
            ActorRef actorRef = this.actorSystem.actorOf(Props.create(HTMActorImpl.class),
                    "HTMActor" + identifier.getDisplay());
            actorRef.tell(identifier, this.actorSystem.guardian());
        }
    }

    public void stop() {
        actorSystem.terminate();
    }

    public void tell(String actorPattern, Object msg) {
        ActorSelection actorSelection = this.actorSystem.actorSelection(getUserPattern(actorPattern));
        actorSelection.tell(msg, ActorRef.noSender());
    }

    // DOESN"T WORK
    public Iterator<String> getNames() {

//        this.actorSystem.
        ActorSelection actorSelection = this.actorSystem.actorSelection(getUserPattern(WILDCARD_ACTOR));

        scala.collection.Iterator<SelectionPathElement> paths = actorSelection.path().seq().iterator();

        return new AbstractIterator<String>() {
            @Override
            protected String computeNext() {
                if (paths.hasNext()) {
                    SelectionPathElement next = paths.next();
                }
                return null;
            }
        };
    }

    private static String getUserPattern(String pattern) {
        return "/user/" + pattern;
    }

    public void start() throws InterruptedException {
        ActorSystem actorSystem = ActorSystem.create("actor-system");

        for (int i = 0; i < ACTOR_COUNT; i++) {
            ActorRef actorRef = actorSystem.actorOf(Props.create(HTMActorImpl.class), "MemoryActor" + (i + 1));
            System.out.println("af: " + actorRef.path());
            actorRef.tell("hi", ActorRef.noSender());

//            Thread.sleep(500);
        }

        System.out.println("as: " + actorSystem);

        ActorSelection actorSel = actorSystem.actorSelection("/user/*");
        System.out.println("_as: " + actorSel);
        actorSel.tell("helloAgain", ActorRef.noSender());

//        for (int i = 0; i < MEMORY_ACTOR_COUNT; i++) {
//            ActorSelection actorSelection = actorSystem.actorSelection("/user/MemoryActor" + (i + 1));
//            System.out.println("_af: " + actorSelection);
//        }
        Thread.sleep(5000);
        System.out.println("Actor System Shutdown Starting...");

        actorSystem.terminate();
    }

    public static void main(String[] args) throws InterruptedException {
        Set<Identifier> identifiers = Sets.newHashSet(
                new Identifier(0, 0),
                new Identifier(1, 1),
                new Identifier(2, 2));

        HTMActorController actorController = new HTMActorController("actor-system", identifiers);
        try {
            actorController.tell("*", "hi");
            actorController.tell("HTMActor1_1", "ho");
        } finally {
            actorController.stop();
        }

    }
}
