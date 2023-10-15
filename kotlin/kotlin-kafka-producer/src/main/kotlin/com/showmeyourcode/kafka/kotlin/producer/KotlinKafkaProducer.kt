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
    val numberOfMessages: Long,
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(KotlinKafkaProducer::class.java)
    }

    fun produce() {
        logger.info("Starting a Kotlin producer...")
        val time = System.currentTimeMillis()

        try {
            for (index in 1 until numberOfMessages + 1) {
                // You can specify a partition or not.
                // If no partition is specified, Kafka will use a round-robin partition assignment.
                // If the ordering of the messages matters for you, you should specify a key.
                val record: ProducerRecord<Long, String> =
                    ProducerRecord(
                        KafkaProperties.TOPIC,
                        index,
                        "Hello World $index Kotlin",
                    )
                produceMessage(record, time)
            }
        } finally {
            // The data produced by a producer are asynchronous.
            // Therefore, two additional functions, i.e., flush() and close() are required to ensure
            // the producer is shut down after the message is sent to Kafka.
            // The flush() will force all the data that was in . send() to be produced
            // and close() stops the producer.
            producer.flush()
            producer.close()
        }
    }

    private fun produceMessage(
        record: ProducerRecord<Long, String>,
        time: Long,
    ) {
        logger.info("Sending a record: (key=${record.key()} value=${record.value()} partition=${record.partition()})")

        producer.send(record) { metadata, exception ->
            if (exception == null) {
                val elapsedTime = System.currentTimeMillis() - time
                logger.info(
                    """The record metadata: 
                                    |key=${record.key()} partition=${metadata.partition()}, 
                                    |offset=${metadata.offset()} time=$elapsedTime
                                    |topic=${record.topic()} value=${record.value()}
                    """.trimMargin(),
                )
            } else {
                logger.error("Cannot produce a message! ", exception)
            }
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
