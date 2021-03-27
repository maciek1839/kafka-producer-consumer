package com.showmeyourcode.kafka.common

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.LongSerializer
import org.apache.kafka.common.serialization.StringSerializer
import java.util.*

class KafkaProperties {
    companion object {
        const val TOPIC = "my-example-topic"

        /*
        If you have more brokers, use comma as a separator for instance:
        const val BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093"
         */
        const val BOOTSTRAP_SERVERS = "localhost:9092"
    }
}
