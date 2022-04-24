package com.showmeyourcode.kafka.kotlin_spring.starter;

import com.showmeyourcode.kafka.kotlin_spring.starter.model.KafkaMessage;
import com.showmeyourcode.kafka.kotlin_spring.starter.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "application", name = "is-producer-enabled", havingValue = "true")
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final String instanceId = ApplicationService.getInstanceIDAsString();

    @Value("${topic.name}")
    private String topicName;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostConstruct
    private void setup() {
        log.info("Kafka producer initialized.");
    }

    @Scheduled(fixedRate = 10000)
    public void scheduleKafkaProducer() {
        var message = new KafkaMessage(instanceId, counter.getAndIncrement());
        log.info("Producer ({}) message: {}", instanceId, message);
        kafkaTemplate.send(topicName, message.toString());
    }
}
