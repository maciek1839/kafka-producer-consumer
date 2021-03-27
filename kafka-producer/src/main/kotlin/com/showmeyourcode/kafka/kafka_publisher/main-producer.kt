package com.showmeyourcode.kafka.kafka_publisher

import com.showmeyourcode.kafka.kafka_publisher.ExampleKafkaProducer.Companion.runProducer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("com.showmeyourcode.kafka.kafka_publisher.main-producer")

fun main(args: Array<String>) {
    logger.info("Starting a Kafka producer...")
    if (args.isEmpty()) {
        runProducer(5);
    } else {
        runProducer(Integer.parseInt(args[0]));
    }
}
