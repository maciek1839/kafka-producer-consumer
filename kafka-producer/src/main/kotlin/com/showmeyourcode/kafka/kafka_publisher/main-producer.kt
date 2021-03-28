package com.showmeyourcode.kafka.kafka_publisher

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("com.showmeyourcode.kafka.kafka_publisher.main-producer")

fun main(args: Array<String>) {
    logger.info("Starting a Kafka producer...")

    if (args.isEmpty()) {
        val numberOfMessages = 5
        ExampleKafkaProducer.runProducer(numberOfMessages)
        ExampleKafkaProducerAvro.runProducer(numberOfMessages)
    } else {
        val numberOfMessages = Integer.parseInt(args[0])
        ExampleKafkaProducer.runProducer(numberOfMessages)
        ExampleKafkaProducer.runProducer(numberOfMessages)
    }
}
