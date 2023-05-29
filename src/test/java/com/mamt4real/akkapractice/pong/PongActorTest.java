package com.mamt4real.akkapractice.pong;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import static akka.pattern.Patterns.ask;

import akka.actor.Props;
import org.junit.Test;
import scala.concurrent.Future;

import static scala.compat.java8.FutureConverters.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


public class PongActorTest {
    ActorSystem system = ActorSystem.create();
    ActorRef actorRef =
            system.actorOf(Props.create(JavaPongActor.class));

    public CompletionStage<String> askPong(String message) {
        Future sFuture = ask(actorRef, message, 1000);
        CompletionStage<String> cs = toJava(sFuture);
        return cs;
    }

    @Test
    public void shouldReplyToPingWithPong() throws Exception {
        Future sFuture = ask(actorRef, "Ping", 1000);
        final CompletionStage<String> cs = toJava(sFuture);
        final CompletableFuture<String> jFuture =
                (CompletableFuture<String>) cs;
        assert (jFuture.get(1000, TimeUnit.MILLISECONDS).
                equals("Pong"));
    }

    @Test(expected = ExecutionException.class)
    public void shouldReplyToUnknownMessageWithFailure() throws
            Exception {
        Future sFuture = ask(actorRef, "unknown", 1000);
        final CompletionStage<String> cs = toJava(sFuture);
        final CompletableFuture<String> jFuture =
                (CompletableFuture<String>) cs;
        jFuture.get(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void printToConsole() throws Exception {
        askPong("Ping").
                thenAccept(x -> System.out.println("replied with: " + x));
        Thread.sleep(100);
    }
}