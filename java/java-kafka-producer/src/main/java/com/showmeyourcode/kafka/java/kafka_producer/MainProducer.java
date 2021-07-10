package com.showmeyourcode.kafka.java.kafka_producer;

public class MainProducer {
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
    }
}
