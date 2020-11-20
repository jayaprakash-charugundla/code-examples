package com.jc.spring.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(id = "class-level", topics = "jc-topic-1")
public class KafkaClassListener {
    @KafkaHandler
    void listen(String message) {
        log.info("ClassLevel KafkaHandler[String] {}", message);
    }

    @KafkaHandler(isDefault = true)
    void listenDefault(Object object) {
        log.info("ClassLevel KafkaHandler[Default] {}", object);
    }
}
