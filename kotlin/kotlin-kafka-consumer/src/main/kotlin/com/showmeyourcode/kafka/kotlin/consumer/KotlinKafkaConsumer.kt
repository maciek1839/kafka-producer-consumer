package com.showmeyourcode.kafka.kotlin.consumer

import com.showmeyourcode.kafka.kotlin.common.KafkaProperties
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*

// The class could be just KafkaConsumer, although to avoid class name clashes as Kafka has its own KafkaConsumer, "Kotlin" prefix is used.
class KotlinKafkaConsumer internal constructor(
    private val kafkaConsumer: Consumer<String, String>,
    val numberOfMessagesToConsume: Long,
) {
    companion object {
        private var logger: Logger = LoggerFactory.getLogger(KotlinKafkaConsumer::class.java)
    }

    fun consume() {
        logger.info("Starting a Kotlin consumer...")
        val topics = listOf(KafkaProperties.TOPIC)

        try {
            var currentMessageNumber = 0

            kafkaConsumer.subscribe(topics)

            while (currentMessageNumber++ < numberOfMessagesToConsume) {
                val records: ConsumerRecords<String, String> = kafkaConsumer.poll(Duration.ofMinutes(10))
                for (record in records) {
                    logger.info("Kotlin - Consuming record {}: {}", currentMessageNumber, record)
                }
            }
        } finally {
            kafkaConsumer.close()
        }
    }

    class KotlinKafkaConsumerBuilder {
        private var numberOfMessages: Long = 0

        fun withNumberOfMessages(numberOfMessages: Long) = apply { this.numberOfMessages = numberOfMessages }

        fun build(): KotlinKafkaConsumer {
            val properties = Properties()
            properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaProperties.BOOTSTRAP_SERVERS
            properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
            properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
            // GROUP_ID_CONFIG: The consumer group id used to identify to which group this consumer belongs.
            properties[ConsumerConfig.GROUP_ID_CONFIG] = KafkaProperties.CONSUMER_GROUP_ID

            return KotlinKafkaConsumer(
                org.apache.kafka.clients.consumer.KafkaConsumer(properties),
                numberOfMessages,
            )
        }
    }
}

// You can add a main method here to run a single consumer.
