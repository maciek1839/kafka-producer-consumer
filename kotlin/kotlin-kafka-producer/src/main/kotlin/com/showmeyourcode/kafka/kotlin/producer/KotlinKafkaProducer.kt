package com.showmeyourcode.kafka.kotlin.producer

import com.showmeyourcode.kafka.kotlin.common.KafkaProperties
import org.apache.kafka.clients.producer.*
import org.apache.kafka.common.serialization.LongSerializer
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class KotlinKafkaProducer {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(KotlinKafkaProducer::class.java);

        private fun getProperties(): Properties {
            val props = Properties()
            props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaProperties.BOOTSTRAP_SERVERS
            props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = LongSerializer::class.java.name
            props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
            // CLIENT_ID_CONFIG: Id of the producer so that the broker can determine the source of the request.
            props[ProducerConfig.CLIENT_ID_CONFIG] = KafkaProperties.PRODUCER_CLIENT_ID
            return props
        }

        private fun createProducer(): Producer<Long, String> {
            logger.info("Creating a Kotlin producer...")
            return KafkaProducer(getProperties())
        }

        fun runProducer(sendMessageCount: Int) {
            logger.info("Starting a Kotlin producer...")
            val producer: Producer<Long, String> = createProducer()
            val time = System.currentTimeMillis()
            try {
                for (index in time until time + sendMessageCount) {
                    val record: ProducerRecord<Long, String> = ProducerRecord(
                        KafkaProperties.TOPIC,
                        index,
                        "Hello World $index Kotlin"
                    )
                    val metadata: RecordMetadata = producer.send(record).get()
                    val elapsedTime = System.currentTimeMillis() - time
                    logger.info("Sending a record: ${record.key()}(key=${record.value()} value=${metadata.partition()})")
                    logger.info("The record metadata: partition=${metadata.partition()}, offset=${metadata.offset()}) time=${elapsedTime}")
                }
            } finally {
                producer.flush()
                producer.close()
            }
        }
    }
}
