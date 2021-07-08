package com.showmeyourcode.kafka.kotlin.common

class KafkaKotlinProperties {
    companion object {
        const val TOPIC = "kotlin-example-topic"

        /*
        If you have more brokers, use comma as a separator for instance:
        const val BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093"
         */
        const val BOOTSTRAP_SERVERS = "localhost:9092"

        const val CONSUMER_GROUP_ID = "kotlin-test-group"
        const val PRODUCER_CLIENT_ID = "KotlinKafkaProducer"
        const val PRODUCER_AVRO_CLIENT_ID = "KotlinKafkaAvroProducer"

        const val AVRO_SCHEMA_REGISTRY = "http://localhost:8081"
    }
}
