package com.showmeyourcode.kafka.java_spring.messaging;

import com.showmeyourcode.kafka.java_spring.service.IdentityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "is-consumer-enabled", havingValue = "true")
public class KafkaConsumer {

    private final IdentityService applicationService;

    @PostConstruct
    private void setup() {
        log.info("Kafka consumer initialized.");
    }

    @KafkaListener(topics = "spring.kafka.topic", groupId = "group_id")
    public void scheduleKafkaConsumer(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition
    ) {
        log.info("Consumer ({}, partition: {}) received message: {} ",
                applicationService.getId(),
                partition,
                message
        );
    }
}
