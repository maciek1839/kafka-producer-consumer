package com.showmeyourcode.kafka.kotlin.producer

import com.showmeyourcode.kafka.kotlin.common.KafkaProperties
import org.apache.kafka.clients.producer.*
import org.apache.kafka.common.serialization.LongSerializer
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

// The class could be just KafkaProducer, although to avoid class name clashes as Kafka has its own KafkaProducer, "Kotlin" prefix is used.
class KotlinKafkaProducer internal constructor(
    private val producer: Producer<Long, String>,
    val numberOfMessages: Long
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(KotlinKafkaProducer::class.java)
    }

    fun produce() {
        logger.info("Starting a Kotlin producer...")
        val time = System.currentTimeMillis()

        try {
            for (index in 1 until numberOfMessages + 1) {
                val record: ProducerRecord<Long, String> = ProducerRecord(
                    KafkaProperties.TOPIC,
                    index,
                    "Hello World $index Kotlin"
                )
                logger.info("Sending a record $index: ${record.key()}(key=${record.value()})")

                val metadata: RecordMetadata = producer.send(record).get()

                val elapsedTime = System.currentTimeMillis() - time
                logger.info("The record metadata: partition=${metadata.partition()}, offset=${metadata.offset()}) time=$elapsedTime")
            }
        } catch (e: Exception) {
            throw KafkaProducerException("Cannot produce Kafka messages. Kotlin Kafka error: ${e.message}", e)
        } finally {
            producer.flush()
            producer.close()
        }
    }

    class KotlinKafkaProducerBuilder {
        private var numberOfMessages: Long = 0

        fun withNumberOfMessages(numberOfMessages: Long) = apply { this.numberOfMessages = numberOfMessages }

        fun build(): KotlinKafkaProducer {
            val props = Properties()
            props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaProperties.BOOTSTRAP_SERVERS
            props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = LongSerializer::class.java.name
            props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
            // CLIENT_ID_CONFIG: ID of the producer so that the broker can determine the source of the request.
            props[ProducerConfig.CLIENT_ID_CONFIG] = KafkaProperties.PRODUCER_CLIENT_ID

            return KotlinKafkaProducer(KafkaProducer(props), numberOfMessages)
        }
    }
}
