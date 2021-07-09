package com.showmeyourcode.kafka.kotlin.kafka_producer

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        val numberOfMessages = 5
        ExampleKafkaProducer.runProducer(numberOfMessages)
        ExampleKafkaProducerAvro.runProducer(numberOfMessages)
    } else {
        val numberOfMessages = Integer.parseInt(args[0])
        ExampleKafkaProducer.runProducer(numberOfMessages)
        ExampleKafkaProducerAvro.runProducer(numberOfMessages)
    }
}
