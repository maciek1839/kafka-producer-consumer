package com.showmeyourcode.kafka.kafka_publisher

import com.showmeyourcode.kafka.common.KafkaProperties
import org.apache.kafka.clients.producer.*
import org.apache.kafka.common.serialization.LongSerializer
import org.apache.kafka.common.serialization.StringSerializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class ExampleKafkaProducer {

    companion object {
        private var logger: Logger = LoggerFactory.getLogger(ExampleKafkaProducer::class.java);

        private fun getProperties():Properties{
            val props = Properties()
            props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaProperties.BOOTSTRAP_SERVERS
            props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = LongSerializer::class.java.name
            props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
            props[ProducerConfig.CLIENT_ID_CONFIG] = "KafkaExampleProducer"
            return props
        }

        private fun createProducer(): Producer<Long, String> {
            logger.info("Creating a Kafka producer...")
            return KafkaProducer(getProperties())
        }

        fun runProducer(sendMessageCount: Int) {
            logger.info("Starting a producer...")
            val producer: Producer<Long, String> = createProducer()
            val time = System.currentTimeMillis()
            try {
                for (index in time until time + sendMessageCount) {
                    val record: ProducerRecord<Long, String> = ProducerRecord(
                        KafkaProperties.TOPIC,
                        index,
                        "Hello World $index"
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
