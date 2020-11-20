package com.jc.spring.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaListenerExample {

    @KafkaListener(topics = "jc-topic-1")
    void listener(String message) {
        log.info("Listener [{}]", message);
    }

    @KafkaListener(topics = {"jc-topic-1", "jc-topic-2"}, groupId = "jc-group-2")
    void commonListenerForMultipleTopics(String message) {
        log.info("MultipleTopicListener - [{}]", message);
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = "jc-topic-3", partitionOffsets = {
        @PartitionOffset(partition = "0", initialOffset = "0")}), groupId = "jc-group-3")
    void listenToPartitionWithOffset(@Payload String message,
                                     @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                     @Header(KafkaHeaders.OFFSET) int offset) {
        log.info("ListenToPartitionWithOffset [{}] from partition-{} with offset-{}", message, partition, offset);
    }

    @KafkaListener(topics = "jc-bytes")
    void listenerForRoutingTemplate(String message) {
        log.info("RoutingTemplate BytesListener [{}]", message);
    }

    @KafkaListener(topics = "jc-others")
    @SendTo("jc-topic-2")
    String listenAndReply(String message) {
        log.info("ListenAndReply [{}]", message);
        return "This is a reply sent to 'jc-topic-2' topic after receiving message at 'jc-others' topic";
    }

    @KafkaListener(id = "1", topics = "jc-user", groupId = "jc-user-mc",
        containerFactory = "userKafkaListenerContainerFactory")
    void listenerWithMessageConverter(User user) {
        log.info("MessageConverterUserListener [{}]", user);
    }
}
