package com.mamt4real.akkapractice;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
    public static void main(String... args) {
        ActorSystem system = ActorSystem.create("akkademy");
        system.actorOf(Props.create(AkkademyDb.class), "akkademy-db");
    }
}