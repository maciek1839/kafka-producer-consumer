package com.showmeyourcode.kafka.java.kafka_consumer;

import com.showmeyourcode.kafka.java.common.KafkaProperties;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Getter
// The class could be just KafkaConsumer, although to avoid class name clashes as Kafka has its own KafkaConsumer, "Java" prefix is used.
public class JavaKafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(JavaKafkaConsumer.class);

    @Getter(AccessLevel.NONE)
    @SuppressWarnings("java:S1700")
    private final Consumer<String, String> consumer;
    private final Long numberOfMessagesToConsume;

    JavaKafkaConsumer(Consumer<String, String> consumer, Long numberOfMessagesToConsume) {
        this.consumer = consumer;
        this.numberOfMessagesToConsume = numberOfMessagesToConsume;
    }

    public void consume() {
        logger.info("Starting a Java consumer...");

        var topics = List.of(KafkaProperties.TOPIC);

        try (consumer) {
            long currentMessageNumber = 0L;

            consumer.subscribe(topics);

            while (currentMessageNumber++ < numberOfMessagesToConsume) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMinutes(10));
                for (ConsumerRecord<String, String> consumerRecord : records) {
                    logger.info("Java - Consuming record {}: {}", currentMessageNumber, consumerRecord);
                }
            }

        } finally{
            consumer.close();
        }
    }

    public static class JavaKafkaConsumerBuilder {

        private long numberOfMessages;

        public JavaKafkaConsumer build() {
            final var props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.BOOTSTRAP_SERVERS);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaProperties.CONSUMER_GROUP_ID);

            return new JavaKafkaConsumer(
                    new org.apache.kafka.clients.consumer.KafkaConsumer<>(props),
                    numberOfMessages
            );
        }

        public JavaKafkaConsumerBuilder withNumberOfMessages(long numberOfMessages) {
            this.numberOfMessages = numberOfMessages;
            return this;
        }
    }
}
