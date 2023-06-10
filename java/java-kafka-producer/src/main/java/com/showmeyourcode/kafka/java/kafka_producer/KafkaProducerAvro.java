package com.showmeyourcode.kafka.java.kafka_producer;

import com.showmeyourcode.kafka.java.common.KafkaProperties;
import com.showmeyourcode.kafka.java.kafka_producer.avro.ExampleUserRecord;
import com.showmeyourcode.kafka.java.kafka_producer.avro.ExampleUserRecord2;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.function.Function;

public class KafkaProducerAvro {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerAvro.class);
    private final Producer<Long, GenericRecord> producer;
    private final long numberOfMessagesToProduce;
    private final String producerName;
    private final Function<Void, Schema> getSchema;

    KafkaProducerAvro(Producer<Long, GenericRecord> producer,
                      long numberOfMessagesToProduce,
                      String producerName,
                      Function<Void, Schema> getSchema) {
        this.producer = producer;
        this.numberOfMessagesToProduce = numberOfMessagesToProduce;
        this.producerName = producerName;
        this.getSchema = getSchema;
    }

    public void produce() throws KafkaProducerException {
        logger.info("Starting an Avro producer - {}", producerName);
        long time = System.currentTimeMillis();
        try {
            Schema schema = this.getSchema.apply(null);
            var schemaAsString = schema.toString(true);
            logger.info("Generated schema: {}", schemaAsString);
            for (long index = 0; index < numberOfMessagesToProduce; index++) {
                var dataRecord = new ExampleUserRecord("name", index, "100 100 101");
                GenericRecord avroRecord = new GenericData.Record(schema);
                avroRecord.put("name", dataRecord.getName());
                avroRecord.put("age", dataRecord.getAge());
                avroRecord.put("phoneNumber", dataRecord.getPhoneNumber());

                ProducerRecord<Long, GenericRecord> producerRecord = new ProducerRecord<>(
                        KafkaProperties.TOPIC,
                        index,
                        avroRecord
                );
                logger.info("Sending an Avro record: (key={} value={})",
                        producerRecord.key(),
                        producerRecord.value()
                );
                // todo: make it non-blocking!
                RecordMetadata metadata = producer.send(producerRecord).get();
                long elapsedTime = System.currentTimeMillis() - time;
                logger.info("An Avro record metadata: partition={}, offset={}) time={}",
                        metadata.partition(),
                        metadata.offset(),
                        elapsedTime
                );
            }
        } catch (Exception err) {
            throw new KafkaProducerException(String.format("Cannot produce an Avro message! Error: %s", err.getMessage()), err);
        } finally {
            producer.flush();
            producer.close();
        }
    }

    public static class KafkaProducerAvroBuilder {

        private long numberOfMessages;
        private Function<Void, Schema> getSchema;
        private String name;

        public KafkaProducerAvro build() {
            var props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.BOOTSTRAP_SERVERS);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
            // CLIENT_ID_CONFIG: Id of the producer so that the broker can determine the source of the request.
            props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaProperties.PRODUCER_AVRO_CLIENT_ID);

            // Avro serializer property - the address of Schema Registry
            props.put("schema.registry.url", KafkaProperties.AVRO_SCHEMA_REGISTRY);

            return new KafkaProducerAvro(
                    new org.apache.kafka.clients.producer.KafkaProducer<>(props),
                    numberOfMessages,
                    name,
                    getSchema
            );
        }

        public KafkaProducerAvro.KafkaProducerAvroBuilder withAvroClass() {
            this.getSchema = new Function<Void, Schema>() {
                @Override
                public Schema apply(Void unused) {
                    return ReflectData.get().getSchema(ExampleUserRecord.class);
                }
            };
            return this;
        }

        public KafkaProducerAvro.KafkaProducerAvroBuilder withAvroClassFromFile() {
            this.getSchema = new Function<Void, Schema>() {
                @Override
                public Schema apply(Void unused) {
                    return ReflectData.get().getSchema(ExampleUserRecord2.class);
                }
            };
            return this;
        }

        public KafkaProducerAvro.KafkaProducerAvroBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public KafkaProducerAvro.KafkaProducerAvroBuilder withNumberOfMessage(long numberOfMessage) {
            this.numberOfMessages = numberOfMessage;
            return this;
        }
    }
}
