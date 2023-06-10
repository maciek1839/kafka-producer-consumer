package com.showmeyourcode.kafka.java.kafka_producer;

import com.showmeyourcode.kafka.java.common.KafkaProperties;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public final class KafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);
    private final Producer<Long, String> producer;
    private final Long numberOfMessagesToProduce;

    KafkaProducer(Producer<Long, String> producer, Long numberOfMessagesToProduce) {
        this.producer = producer;
        this.numberOfMessagesToProduce = numberOfMessagesToProduce;
    }

    public void produce() throws KafkaProducerException {
        logger.info("Starting a Java producer...");
        long time = System.currentTimeMillis();
        try {
            for (long index = 0; index < numberOfMessagesToProduce; index++) {
                ProducerRecord<Long, String> producerRecord = new ProducerRecord<>(
                        KafkaProperties.TOPIC,
                        index,
                        String.format("Hello World %d Java", index)
                );
                logger.info("Sending a record: (key={} value={})",
                        producerRecord.key(),
                        producerRecord.value()
                );
                RecordMetadata metadata = producer.send(producerRecord).get();
                long elapsedTime = System.currentTimeMillis() - time;
                logger.info("The record metadata: partition={}, offset={}) time={}",
                        metadata.partition(),
                        metadata.offset(),
                        elapsedTime
                );
            }
        } catch (Exception e) {
            throw new KafkaProducerException(String.format("Cannot produce a record!  Kafka error: %s", e.getMessage()), e);
        } finally {
            producer.flush();
            producer.close();
        }
    }

    public static class KafkaProducerBuilder {

        private long numberOfMessages;

        public KafkaProducer build() {
            var props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.BOOTSTRAP_SERVERS);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            // CLIENT_ID_CONFIG: Id of the producer so that the broker can determine the source of the request.
            props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaProperties.PRODUCER_CLIENT_ID);

            return new KafkaProducer(
                    new org.apache.kafka.clients.producer.KafkaProducer<>(props),
                    numberOfMessages
            );
        }

        public KafkaProducerBuilder withNumberOfMessage(long numberOfMessage) {
            this.numberOfMessages = numberOfMessage;
            return this;
        }
    }
}
