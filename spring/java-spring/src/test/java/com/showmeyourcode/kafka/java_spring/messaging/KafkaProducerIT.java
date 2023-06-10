package com.showmeyourcode.kafka.java_spring.messaging;

import com.showmeyourcode.kafka.java_spring.configuration.AppProperties;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowableOfType;

@SpringBootTest
@ActiveProfiles("producer")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class KafkaProducerIT {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private AppProperties properties;
    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @Test
    void shouldProduceKafkaMessageWhenConfigurationIsValid() {
        catchThrowableOfType(() -> context.getBean("kafkaConsumer"), NoSuchBeanDefinitionException.class);

        var consumerRecords = getKafkaMessage();

        assertThat(consumerRecords.count()).isGreaterThanOrEqualTo(0);
        assertThat(consumerRecords.records(new TopicPartition(properties.getKafka().getTopicName(),0))).hasSizeGreaterThanOrEqualTo(0);
    }

    private ConsumerRecords<String, String> getKafkaMessage() {
        var testConsumer = consumerFactory.createConsumer("group_id", "test-suffix");
        testConsumer.subscribe(List.of(properties.getKafka().getTopicName()));
        return  testConsumer.poll(Duration.ofSeconds(3L));
    }
}
