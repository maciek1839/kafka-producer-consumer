package com.showmeyourcode.kafka.kotlin.kafka_consumer

import com.showmeyourcode.kafka.kotlin.common.KafkaKotlinProperties
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class ExampleKafkaConsumer {
    companion object {
        private var logger: Logger = LoggerFactory.getLogger(ExampleKafkaConsumer::class.java);

        private fun createConsumer(): KafkaConsumer<String, String> {
            val properties = Properties()
            properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaKotlinProperties.BOOTSTRAP_SERVERS
            properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
            properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
            //GROUP_ID_CONFIG: The consumer group id used to identify to which group this consumer belongs.
            properties[ConsumerConfig.GROUP_ID_CONFIG] = KafkaKotlinProperties.CONSUMER_GROUP_ID
            return KafkaConsumer<String, String>(properties);
        }

        fun runConsumer() {
            logger.info("Starting a Kotlin consumer...")
            val topics: MutableList<String> = ArrayList()
            topics.add(KafkaKotlinProperties.TOPIC)
            val kafkaConsumer = createConsumer()
            kafkaConsumer.subscribe(topics)
            try {
                while (true) {
                    val records: ConsumerRecords<String, String> = kafkaConsumer.poll(10)
                    for (record in records) {
                        println(String.format("Topic - %s, Partition - %d, Value: %s", record.topic(), record.partition(), record.value()))
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
