package com.showmeyourcode.kafka.kotlin.starter

import com.showmeyourcode.kafka.kotlin.consumer.KotlinKafkaConsumer
import com.showmeyourcode.kafka.kotlin.producer.KotlinKafkaAvroWithRegistryProducer
import com.showmeyourcode.kafka.kotlin.producer.KotlinKafkaProducer

fun main(args: Array<String>) {
    val numberOfMessages =
        if (args.isEmpty()) {
            10L
        } else {
            args[0].toLong()
        }

    KotlinKafkaProducer.KotlinKafkaProducerBuilder().withNumberOfMessages(numberOfMessages).build().produce()
    KotlinKafkaAvroWithRegistryProducer.KotlinKafkaAvroWithRegistryProducerBuilder().withNumberOfMessages(
        numberOfMessages,
    ).build().produce()

    KotlinKafkaConsumer.KotlinKafkaConsumerBuilder().withNumberOfMessages(numberOfMessages).build().consume()
}
