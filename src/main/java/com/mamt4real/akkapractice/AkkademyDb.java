package com.mamt4real.akkapractice;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import com.mamt4real.akkapractice.messages.GetRequest;
import com.mamt4real.akkapractice.messages.KeyNotFoundException;
import com.mamt4real.akkapractice.messages.SetRequest;

import java.util.HashMap;
import java.util.Map;

public class AkkademyDb extends AbstractActor {
    protected final LoggingAdapter log = Logging.getLogger(context().system(), this);

    protected final Map<String, Object> map = new HashMap<>();

    private AkkademyDb(){
        receive(ReceiveBuilder
                .match(SetRequest.class, message -> {
                    log.info("Received set request – key: {} value: {}", message.getKey(), message.getValue());
                    map.put(message.getKey(), message.
                            getValue());
                   sender().tell(new Status.Success(message.getKey()), self());
                })
                .match(GetRequest.class, message -> {
                    log.info("Received Get request:  key {}", message.getKey());
                    Object response = map.containsKey(message.getKey())?
                            new Status.Success(map.get(message.getKey())):
                            new Status.Failure(new KeyNotFoundException(message.getKey()));
                    sender().tell(response, self());
                })
                .matchAny(o -> {
                    log.info("received unknown message {}", o);
                    sender().tell(new Status.Failure(new ClassNotFoundException()), self());
                }).build()
        );
    }


}
