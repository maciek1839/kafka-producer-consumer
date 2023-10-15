package com.showmeyourcode.kafka.java_spring.service.messaging;

import com.showmeyourcode.kafka.java_spring.configuration.AppProperties;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowableOfType;

@SpringBootTest
@ActiveProfiles({"test-kafka","producer"})
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"${app.kafka.topic-name}"})
class KafkaProducerServiceIT {

    @Value(value = "${spring.kafka.producer.group-id}")
    private String producerGroup;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private AppProperties properties;
    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @Test
    void shouldProduceKafkaMessageWhenConfigurationIsValid() {
        catchThrowableOfType(() -> context.getBean("kafkaConsumerService"), NoSuchBeanDefinitionException.class);

        var consumerRecords = getKafkaMessage();

        assertThat(consumerRecords.count()).isNotZero();
        assertThat(
                consumerRecords.records(new TopicPartition(properties.getKafka().getTopicName(),0))
        ).hasSizeGreaterThan(0);
    }

    private ConsumerRecords<String, String> getKafkaMessage() {
        var testConsumer = consumerFactory.createConsumer(producerGroup, "test-suffix");
        testConsumer.subscribe(List.of(properties.getKafka().getTopicName()));
        return testConsumer.poll(Duration.ofSeconds(5L));
    }
}
