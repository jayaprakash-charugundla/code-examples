package com.jc.spring.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaDemo {

    @Autowired
    private KafkaSenderExample kafkaSenderExample;

    @EventListener
    void init(ApplicationReadyEvent event) throws InterruptedException {
        log.info("---------------------------------");
        kafkaSenderExample.sendMessage("jc-topic-1",
            "I'll be received by MultipleTopicListener, Listener & ClassLevel KafkaHandler");

        log.info("---------------------------------");
        kafkaSenderExample.sendMessage("jc-topic-3",
            "I'll be received by ListenToPartitionWithOffset");

        log.info("---------------------------------");
        kafkaSenderExample.sendMessageWithCallback("jc-others", "I'll get a async Callback");

        log.info("---------------------------------");
        kafkaSenderExample.sendWithRoutingTemplate("jc-bytes", "I'm sent using RoutingTemplate");

        log.info("---------------------------------");
        kafkaSenderExample.sendMessage("jc-topic-3", "I'll be ignored by RecordFilter");

        log.info("---------------------------------");
        kafkaSenderExample.sendMessage("jc-others", "I will get reply back from @SendTo");

        log.info("---------------------------------");
        kafkaSenderExample.sendCustomMessage("jc-user", new User("Jayaprakash"));
    }
}
