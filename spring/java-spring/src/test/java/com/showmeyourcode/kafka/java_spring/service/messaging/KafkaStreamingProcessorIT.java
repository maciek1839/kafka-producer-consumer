package com.showmeyourcode.kafka.java_spring.service.messaging;

import com.showmeyourcode.kafka.java_spring.configuration.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles({"test-kafka", "streaming"})
@EmbeddedKafka(
        topics = {
                "${app.kafka-streaming.input-topic}",
                "${app.kafka-streaming.output-topic}",
                "spring-streaming-app-counts-repartition"},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"}
)
class KafkaStreamingProcessorIT {

    @Autowired
    private AppProperties properties;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Consumer<String, String> consumer;

    @BeforeAll
    void setUp() {
        // Configure consumer manually
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        consumer = new KafkaConsumer<>(consumerProps);
        consumer.subscribe(Collections.singletonList(properties.getKafka().getTopicName()));
    }

    @AfterAll
    void tearDown() {
        consumer.close();
    }

    // This test will produce some errors in logs as it needs time to start the broker.
    // 2025-01-31 10:36:05.768 [kafka-producer-network-thread | producer-1] WARN  org.apache.kafka.clients.NetworkClient - [Producer clientId=producer-1] Error while fetching metadata with correlation id 6 : {spring.kafka.topic=LEADER_NOT_AVAILABLE}
    @Test
    void shouldProduceMessagesToOutputTopic() {
        // Send test message to input topic
        var message = String.format("Hello Kafka Streams - %s", Instant.now());
        kafkaTemplate.send(properties.getKafka().getTopicName(), message);

        // Await output message
        Awaitility.await().atMost(10, TimeUnit.SECONDS).untilAsserted(() -> {
            ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer);
            List<String> messages = new ArrayList<>();
            records.forEach(record -> messages.add(record.value()));

            assertThat(messages).contains(message);
        });
    }
}
