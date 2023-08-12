package com.showmeyourcode.kafka.java.kafka_producer;

import com.showmeyourcode.kafka.java.common.KafkaProperties;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@Getter
// The class could be just KafkaProducer, although to avoid class name clashes as Kafka has its own KafkaProducer, "Java" prefix is used.
public final class JavaKafkaProducer {
    private static final Logger logger = LoggerFactory.getLogger(JavaKafkaProducer.class);

    @Getter(AccessLevel.NONE)
    private final Producer<Long, String> producer;
    private final Long numberOfMessagesToProduce;

    JavaKafkaProducer(Producer<Long, String> producer, Long numberOfMessagesToProduce) {
        this.producer = producer;
        this.numberOfMessagesToProduce = numberOfMessagesToProduce;
    }

    public void produce() {
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
                producer.send(producerRecord, (metadata, e)->{
                    if(e==null){
                        long elapsedTime = System.currentTimeMillis() - time;
                        logger.info("The record metadata: key={} partition={} offset={} time={}",
                                producerRecord.key(),
                                metadata.partition(),
                                metadata.offset(),
                                elapsedTime
                        );
                    } else {
                            logger.error("Cannot produce a message! ", e);
                    }
                });
            }
        }  finally {
            // The data produced by a producer are asynchronous.
            // Therefore, two additional functions, i.e., flush() and close() are required to ensure
            // the producer is shut down after the message is sent to Kafka.
            // The flush() will force all the data that was in . send() to be produced
            // and close() stops the producer.
            producer.flush();
            producer.close();
        }
    }

    public static class JavaKafkaProducerBuilder {

        private long numberOfMessages;

        public JavaKafkaProducer build() {
            var props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KafkaProperties.BOOTSTRAP_SERVERS);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            // CLIENT_ID_CONFIG: Id of the producer so that the broker can determine the source of the request.
            props.put(ProducerConfig.CLIENT_ID_CONFIG, KafkaProperties.PRODUCER_CLIENT_ID);

            return new JavaKafkaProducer(
                    new org.apache.kafka.clients.producer.KafkaProducer<>(props),
                    numberOfMessages
            );
        }

        public JavaKafkaProducerBuilder withNumberOfMessage(long numberOfMessage) {
            this.numberOfMessages = numberOfMessage;
            return this;
        }
    }
}
