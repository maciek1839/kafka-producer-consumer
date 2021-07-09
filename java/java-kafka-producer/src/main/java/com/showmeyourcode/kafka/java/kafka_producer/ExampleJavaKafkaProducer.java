package com.showmeyourcode.kafka.java.kafka_producer;

import com.showmeyourcode.kafka.java.common.KafkaJavaProperties;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public final class ExampleJavaKafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(ExampleJavaKafkaProducer.class);

    private static Properties getProperties() {
        var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaJavaProperties.BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // CLIENT_ID_CONFIG: Id of the producer so that the broker can determine the source of the request.
        props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaJavaProperties.PRODUCER_CLIENT_ID);
        return props;
    }

    private static Producer<Long, String> createProducer() {
        logger.info("Creating a Java producer...");
        return new KafkaProducer<>(getProperties());
    }

    static void runProducer(int sendMessageCount) {
        logger.info("Starting a Java producer...");
        Producer<Long, String> producer = createProducer();
        long time = System.currentTimeMillis();
        try {
            for (long index = 0; index < sendMessageCount; index++) {
                ProducerRecord<Long, String> producerRecord = new ProducerRecord<>(
                        KafkaJavaProperties.TOPIC,
                        index,
                        String.format("Hello World %d Java",index)
                );
                RecordMetadata metadata = producer.send(producerRecord).get();
                long elapsedTime = System.currentTimeMillis() - time;
                logger.info("Sending a record: {}(key={} value={})",
                        producerRecord.key(),
                        producerRecord.value(),
                        metadata.partition()
                );
                logger.info("The record metadata: partition={}, offset={}) time={}",
                        metadata.partition(),
                        metadata.offset(),
                        elapsedTime
                );
            }
        } catch (Exception e) {
            logger.error("Cannot produce a record! ", e);
        } finally {
            producer.flush();
            producer.close();
        }
    }
}
