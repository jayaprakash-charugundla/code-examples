package com.jc.spring.kafka;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Slf4j
@AllArgsConstructor
@Component
public class KafkaSenderExample {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTemplate<String, User> userKafkaTemplate;
    private final RoutingKafkaTemplate routingKafkaTemplate;

    void sendMessage(String topicName, String message) {
        log.info("Sending : {}", message);
        log.info("--------------------------------");
        kafkaTemplate.send(topicName, message);
    }

    void sendWithRoutingTemplate(String topicName, String message) {
        log.info("Sending : {}", message);
        log.info("--------------------------------");
        routingKafkaTemplate.send(topicName, message.getBytes());
    }

    void sendCustomMessage(String topicName, User user) {
        log.info("Sending Json Serializer : {}", user);
        log.info("--------------------------------");
        //userKafkaTemplate.send(topicName, user);
        routingKafkaTemplate.send(topicName, user);
    }

    void sendMessageWithCallback(String topicName, String message) {
        log.info("Sending : {}", message);
        log.info("---------------------------------");

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Success Callback: [{}] delivered with offset -{}", message,
                    result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                log.warn("Failure Callback: Unable to deliver message [{}]. {}", message, ex.getMessage());
            }
        });
    }

}
