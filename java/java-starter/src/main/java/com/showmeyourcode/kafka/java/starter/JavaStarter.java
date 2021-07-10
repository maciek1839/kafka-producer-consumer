package com.showmeyourcode.kafka.java.starter;

import com.showmeyourcode.kafka.java.kafka_consumer.JavaKafkaConsumer;
import com.showmeyourcode.kafka.java.kafka_producer.JavaKafkaProducer;
import com.showmeyourcode.kafka.java.kafka_producer.JavaKafkaProducerAvroClass;
import com.showmeyourcode.kafka.java.kafka_producer.JavaKafkaProducerAvroFile;

public class JavaStarter {
    public static void main(String... args) {
        int numberOfMessages;
        if (args.length == 0) {
            numberOfMessages = 5;
        } else {
            numberOfMessages = Integer.parseInt(args[0]);
        }
        JavaKafkaProducer.runProducer(numberOfMessages);
        JavaKafkaProducerAvroClass.runProducer(numberOfMessages);
        JavaKafkaProducerAvroFile.runProducer(numberOfMessages);

        JavaKafkaConsumer.runConsumer();
    }
}
