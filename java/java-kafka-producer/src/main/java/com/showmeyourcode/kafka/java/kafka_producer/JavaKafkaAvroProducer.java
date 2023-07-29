package com.showmeyourcode.kafka.java.kafka_producer;

import com.showmeyourcode.kafka.java.common.KafkaProperties;
import com.showmeyourcode.kafka.java.kafka_producer.avro.ExampleUserRecord;
import com.showmeyourcode.kafka.java.kafka_producer.avro.ExampleUserRecord2;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.AccessLevel;
import lombok.Getter;
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

@Getter
public class JavaKafkaAvroProducer {
    private static final Logger logger = LoggerFactory.getLogger(JavaKafkaAvroProducer.class);

    @Getter(AccessLevel.NONE)
    private final Producer<Long, GenericRecord> producer;
    private final long numberOfMessagesToProduce;
    private final String producerName;
    private final Function<Void, Schema> getSchema;

    JavaKafkaAvroProducer(Producer<Long, GenericRecord> producer,
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
                GenericRecord avroRecord = new GenericData.Record(schema);
                avroRecord.put("name", "name");
                avroRecord.put("age", index);
                avroRecord.put("phoneNumber", "100 100 101");

                ProducerRecord<Long, GenericRecord> producerRecord = new ProducerRecord<>(
                        KafkaProperties.TOPIC,
                        index,
                        avroRecord
                );
                logger.info("Sending an Avro record: (key={} value={})",
                        producerRecord.key(),
                        producerRecord.value()
                );

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

    public static class JavaKafkaAvroProducerBuilder {

        private long numberOfMessages;
        private Function<Void, Schema> getSchema;
        private String name;

        public JavaKafkaAvroProducer build() {
            var props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.BOOTSTRAP_SERVERS);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
            // CLIENT_ID_CONFIG: Id of the producer so that the broker can determine the source of the request.
            props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaProperties.PRODUCER_AVRO_CLIENT_ID);

            // Avro serializer property - the address of Schema Registry
            props.put("schema.registry.url", KafkaProperties.AVRO_SCHEMA_REGISTRY);

            return new JavaKafkaAvroProducer(
                    new org.apache.kafka.clients.producer.KafkaProducer<>(props),
                    numberOfMessages,
                    name,
                    getSchema
            );
        }

        public JavaKafkaAvroProducerBuilder withAvroClass() {
            this.getSchema = unused -> ReflectData.get().getSchema(ExampleUserRecord.class);
            return this;
        }

        public JavaKafkaAvroProducerBuilder withAvroClassFromFile() {
            this.getSchema = unused -> ReflectData.get().getSchema(ExampleUserRecord2.class);
            return this;
        }

        public JavaKafkaAvroProducerBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public JavaKafkaAvroProducerBuilder withNumberOfMessage(long numberOfMessage) {
            this.numberOfMessages = numberOfMessage;
            return this;
        }
    }
}
