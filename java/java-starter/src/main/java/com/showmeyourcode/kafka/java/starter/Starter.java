package com.showmeyourcode.kafka.java.starter;

import com.showmeyourcode.kafka.java.kafka_consumer.JavaKafkaConsumer;
import com.showmeyourcode.kafka.java.kafka_consumer.KafkaConsumerException;
import com.showmeyourcode.kafka.java.kafka_producer.JavaKafkaProducer;
import com.showmeyourcode.kafka.java.kafka_producer.JavaKafkaAvroProducer;
import com.showmeyourcode.kafka.java.kafka_producer.KafkaProducerException;

public class Starter {
    public static void main(String... args) throws KafkaConsumerException, KafkaProducerException {
        int numberOfMessages;
        if (args.length == 0) {
            numberOfMessages = 5;
        } else {
            numberOfMessages = Integer.parseInt(args[0]);
        }
        new JavaKafkaProducer.JavaKafkaProducerBuilder().withNumberOfMessage(numberOfMessages).build().produce();
        new JavaKafkaAvroProducer.JavaKafkaAvroProducerBuilder().withNumberOfMessage(numberOfMessages).withAvroClass().withName("Avro Class").build().produce();
        new JavaKafkaAvroProducer.JavaKafkaAvroProducerBuilder().withNumberOfMessage(numberOfMessages).withAvroClassFromFile().withName("Avro File").build().produce();

        new JavaKafkaConsumer.JavaKafkaConsumerBuilder().withNumberOfMessages(Long.MAX_VALUE).build().consume();
    }
}
