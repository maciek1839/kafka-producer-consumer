package com.showmeyourcode.kafka.kotlin.consumer

import com.showmeyourcode.kafka.kotlin.common.KafkaProperties
import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.*

class KotlinKafkaConsumer {
    companion object {
        private var logger: Logger = LoggerFactory.getLogger(KotlinKafkaConsumer::class.java);

        private fun createConsumer(): Consumer<String, String> {
            val properties = Properties()
            properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaProperties.BOOTSTRAP_SERVERS
            properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
            properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
            //GROUP_ID_CONFIG: The consumer group id used to identify to which group this consumer belongs.
            properties[ConsumerConfig.GROUP_ID_CONFIG] = KafkaProperties.CONSUMER_GROUP_ID
            return KafkaConsumer(properties)
        }

        fun runConsumer() {
            logger.info("Starting a Kotlin consumer...")
            val topics: MutableList<String> = ArrayList()
            topics.add(KafkaProperties.TOPIC)
            val kafkaConsumer = createConsumer()
            kafkaConsumer.subscribe(topics)
            try {
                while (true) {
                    val records: ConsumerRecords<String, String> = kafkaConsumer.poll(Duration.ofMinutes(10))
                    for (record in records) {
                        logger.info("Kotlin - Consuming record: {}", record)
                    }
                }
            } catch (e: Exception) {
                logger.error(e.message)
            } finally {
                kafkaConsumer.close()
            }
        }
    }
}
