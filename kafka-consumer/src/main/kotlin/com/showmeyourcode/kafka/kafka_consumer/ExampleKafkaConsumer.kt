package com.showmeyourcode.kafka.kafka_consumer

import com.showmeyourcode.kafka.common.KafkaProperties
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.LongSerializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class ExampleKafkaConsumer {
    companion object {
        private var logger: Logger = LoggerFactory.getLogger(ExampleKafkaConsumer::class.java);

        private fun createConsumer(): KafkaConsumer<String, String> {
            val properties = Properties()
            properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaProperties.BOOTSTRAP_SERVERS
            properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
            properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
            //GROUP_ID_CONFIG: The consumer group id used to identify to which group this consumer belongs.
            properties[ConsumerConfig.GROUP_ID_CONFIG] = "test-group"
            return KafkaConsumer<String, String>(properties);
        }

        fun runConsumer() {
            logger.info("Starting a consumer...")
            val topics: MutableList<String> = ArrayList()
            topics.add(KafkaProperties.TOPIC)
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
