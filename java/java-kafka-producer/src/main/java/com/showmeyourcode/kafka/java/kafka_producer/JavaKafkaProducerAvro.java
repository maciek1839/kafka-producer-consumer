package com.showmeyourcode.kafka.java.kafka_producer;

import com.showmeyourcode.kafka.java.common.JavaKafkaProperties;
import com.showmeyourcode.kafka.java.kafka_producer.avro.ExampleUserRecord;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class JavaKafkaProducerAvro {
    private static final Logger logger = LoggerFactory.getLogger(JavaKafkaProducerAvro.class);

    private static Properties getProperties() {
        var props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, JavaKafkaProperties.BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        // CLIENT_ID_CONFIG: Id of the producer so that the broker can determine the source of the request.
        props.put(ProducerConfig.CLIENT_ID_CONFIG, JavaKafkaProperties.PRODUCER_AVRO_CLIENT_ID);

        // Avro serializer property - the address of Schema Registry
        props.put("schema.registry.url", JavaKafkaProperties.AVRO_SCHEMA_REGISTRY);

        return props;
    }

    private static Producer<Long, GenericRecord> createProducer() {
        logger.info("Creating an Avro Java producer...");
        return new KafkaProducer<>(getProperties());
    }

    public static void runProducer(int sendMessageCount) {
        logger.info("Starting an Avro Kotlin producer...");
        Producer<Long, GenericRecord> producer = createProducer();
        long time = System.currentTimeMillis();
        try {
            var schema = ReflectData.get().getSchema(ExampleUserRecord.class);
            logger.info("Generated schema: {}", schema.toString(true));
            for (long index = 0; index < sendMessageCount; index++) {
                var dataRecord = new ExampleUserRecord("name", index, "100 100 101");
                GenericRecord avroRecord = new GenericData.Record(schema);
                avroRecord.put("name", dataRecord.getName());
                avroRecord.put("age", dataRecord.getAge());
                avroRecord.put("phoneNumber", dataRecord.getPhoneNumber());

                ProducerRecord<Long, GenericRecord> producerRecord = new ProducerRecord<>(
                        JavaKafkaProperties.TOPIC,
                        index,
                        avroRecord
                );
                RecordMetadata metadata = producer.send(producerRecord).get();
                long elapsedTime = System.currentTimeMillis() - time;
                logger.info("Sending an Avro record: {}(key={} value={})",
                        producerRecord.key(),
                        producerRecord.value(),
                        metadata.partition()
                );
                logger.info("The Avro record metadata: partition={}, offset={}) time={}",
                        metadata.partition(),
                        metadata.offset(),
                        elapsedTime
                );
            }
        } catch (Exception err) {
            logger.error("Cannot produce a message! ", err);
        } finally {
            producer.flush();
            producer.close();
        }
    }
}
