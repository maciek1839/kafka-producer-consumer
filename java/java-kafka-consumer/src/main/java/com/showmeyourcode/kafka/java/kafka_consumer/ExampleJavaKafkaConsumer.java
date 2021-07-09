package com.showmeyourcode.kafka.java.kafka_consumer;

import com.showmeyourcode.kafka.java.common.KafkaJavaProperties;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Properties;

public class ExampleJavaKafkaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ExampleJavaKafkaConsumer.class);

    private static Consumer<String, String> createConsumer() {
        final var props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaJavaProperties.BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, KafkaJavaProperties.CONSUMER_GROUP_ID);

        return new KafkaConsumer<>(props);
    }

    static void runConsumer() {
        logger.info("Starting a Java consumer...");
        ArrayList<String> topics = new ArrayList<>();
        topics.add(KafkaJavaProperties.TOPIC);
        Consumer<String, String> kafkaConsumer = createConsumer();
        kafkaConsumer.subscribe(topics);
        try {
            while (true) {

                ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofMinutes(10));
                for (ConsumerRecord<String, String> consumerRecord : records) {
                    logger.info("Java - Consuming record: {}", consumerRecord);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            kafkaConsumer.close();
        }
    }

    public static void main(String... args) {
        runConsumer();
    }
}
