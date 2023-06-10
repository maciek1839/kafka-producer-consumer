package com.showmeyourcode.kafka.java_spring.messaging;

import com.showmeyourcode.kafka.java_spring.configuration.AppProperties;
import com.showmeyourcode.kafka.java_spring.model.KafkaMessage;
import com.showmeyourcode.kafka.java_spring.service.IdentityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "app", name = "is-producer-enabled", havingValue = "true")
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final IdentityService applicationService;
    private final AppProperties properties;
    private final AtomicInteger counter = new AtomicInteger(0);

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate,
                         IdentityService applicationService,
                         AppProperties properties) {
        this.kafkaTemplate = kafkaTemplate;
        this.applicationService = applicationService;
        this.properties = properties;
    }

    @PostConstruct
    private void setup() {
        log.info("Kafka producer initialized.");
    }


    @Scheduled(fixedRateString ="${app.kafka.scheduler-fixed-rate}")
    public void scheduleKafkaProducer() {
        var message = new KafkaMessage(applicationService.getId(), counter.getAndIncrement());
        log.info("Producer ({}) message: {}", applicationService.getId(), message);
        kafkaTemplate.send(properties.getKafka().getTopicName(), message.toString());
    }
}
