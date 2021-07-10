package com.showmeyourcode.kafka.kotlin.kafka_producer

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        val numberOfMessages = 5
        KotlinKafkaProducer.runProducer(numberOfMessages)
        KotlinKafkaProducerAvro.runProducer(numberOfMessages)
    } else {
        val numberOfMessages = Integer.parseInt(args[0])
        KotlinKafkaProducer.runProducer(numberOfMessages)
        KotlinKafkaProducerAvro.runProducer(numberOfMessages)
    }
}
