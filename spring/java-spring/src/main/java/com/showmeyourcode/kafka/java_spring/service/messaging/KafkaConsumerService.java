package com.showmeyourcode.kafka.java_spring.service.messaging;

import com.showmeyourcode.kafka.java_spring.service.IdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "is-consumer-enabled", havingValue = "true")
public class KafkaConsumerService {

    private final IdentityService applicationService;

    @PostConstruct
    private void setup() {
        log.info("Kafka consumer initialized.");
    }

    @KafkaListener(topics = "spring.kafka.topic", groupId = "group_id")
    public void consumeKafkaMessage(
            @Payload String message,
            @Header("kafka_receivedPartitionId") int partition
    ) {
        log.info("Consumer ({}, partition: {}) received message: {} ",
                applicationService.getId(),
                partition,
                message
        );
    }
}
