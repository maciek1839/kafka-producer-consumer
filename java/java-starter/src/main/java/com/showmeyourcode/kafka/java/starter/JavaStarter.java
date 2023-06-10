package com.showmeyourcode.kafka.java.starter;

import com.showmeyourcode.kafka.java.kafka_consumer.KafkaConsumer;
import com.showmeyourcode.kafka.java.kafka_consumer.KafkaConsumerException;
import com.showmeyourcode.kafka.java.kafka_producer.KafkaProducer;
import com.showmeyourcode.kafka.java.kafka_producer.KafkaProducerAvro;
import com.showmeyourcode.kafka.java.kafka_producer.KafkaProducerException;

public class JavaStarter {
    public static void main(String... args) throws KafkaConsumerException, KafkaProducerException {
        int numberOfMessages;
        if (args.length == 0) {
            numberOfMessages = 5;
        } else {
            numberOfMessages = Integer.parseInt(args[0]);
        }
        new KafkaProducer.KafkaProducerBuilder().withNumberOfMessage(numberOfMessages).build().produce();
        new KafkaProducerAvro.KafkaProducerAvroBuilder().withNumberOfMessage(numberOfMessages).withAvroClass().withName("Avro Class").build().produce();
        new KafkaProducerAvro.KafkaProducerAvroBuilder().withNumberOfMessage(numberOfMessages).withAvroClassFromFile().withName("Avro File").build().produce();

        new KafkaConsumer.KafkaConsumerBuilder().withNumberOfMessages(Long.MAX_VALUE).build().consume();
    }
}
