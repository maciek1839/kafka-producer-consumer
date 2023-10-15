package com.showmeyourcode.kafka.java_spring.service.messaging;

import com.showmeyourcode.kafka.java_spring.configuration.AppProperties;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
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
@ActiveProfiles("streaming")
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class KafkaStreamingProcessorIT {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private AppProperties properties;
    @Autowired
    private ConsumerFactory<String, String> consumerFactory;

    @Test
    void shouldProduceMessagesToOutputTopic() {
        var consumerRecords = getKafkaMessage();

        assertThat(consumerRecords.count()).isNotNegative();
        assertThat(consumerRecords.records(new TopicPartition(properties.getKafkaStreaming().getOutputTopic(),0))).hasSizeGreaterThanOrEqualTo(0);
    }

    private ConsumerRecords<String, String> getKafkaMessage() {
        var testConsumer = consumerFactory.createConsumer("group_id", "test-suffix");
        testConsumer.subscribe(List.of(properties.getKafkaStreaming().getOutputTopic()));
        return  testConsumer.poll(Duration.ofSeconds(3L));
    }
}
