package com.showmeyourcode.kafka.kotlin.starter

import com.showmeyourcode.kafka.kotlin.kafka_consumer.KotlinKafkaConsumer
import com.showmeyourcode.kafka.kotlin.kafka_producer.KotlinKafkaProducer
import com.showmeyourcode.kafka.kotlin.kafka_producer.KotlinKafkaProducerAvro

fun main(args: Array<String>) {
    val numberOfMessages = if (args.isEmpty()) {
        5
    } else {
        Integer.parseInt(args[0])
    }

    KotlinKafkaProducer.runProducer(numberOfMessages)
    KotlinKafkaProducerAvro.runProducer(numberOfMessages)

    KotlinKafkaConsumer.runConsumer()
}
