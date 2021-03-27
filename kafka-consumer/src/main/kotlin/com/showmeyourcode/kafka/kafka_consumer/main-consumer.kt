package com.showmeyourcode.kafka.kafka_consumer

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

val logger: Logger = LoggerFactory.getLogger("com.showmeyourcode.kafka.kafka_publisher.main-consumer")

fun main(args: Array<String>) {
    logger.info("Starting a Kafka consumer...")
    ExampleKafkaConsumer.runConsumer();
}
