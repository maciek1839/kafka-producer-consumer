package com.showmeyourcode.kafka.kotlin.producer

import com.github.avrokotlin.avro4k.Avro
import com.showmeyourcode.kafka.kotlin.common.KafkaProperties
import com.showmeyourcode.kafka.kotlin.producer.avro.ExampleUserRecord
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class KotlinKafkaAvroWithRegistryProducer internal constructor(
    private val producer: Producer<Long, GenericRecord>,
    val numberOfMessages: Long,
) {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(KotlinKafkaAvroWithRegistryProducer::class.java)
    }

    // Reference: https://docs.confluent.io/platform/6.0.0/schema-registry/serdes-develop/serdes-avro.html
    fun produce() {
        logger.info("Starting an Avro Kotlin producer...")
        val time = System.currentTimeMillis()
        try {
            val schema: Schema = Avro.schema(ExampleUserRecord.serializer().descriptor)
            logger.info("Generated schema: ${schema.toString(true)}")
            for (index in 1 until numberOfMessages + 1) {
                val dataRecord = ExampleUserRecord("name", index)
                val avroRecord: GenericRecord = GenericData.Record(schema)
                avroRecord.put("name", dataRecord.name)
                avroRecord.put("age", dataRecord.age)
                avroRecord.put("phoneNumber", dataRecord.phoneNumber)

                val record: ProducerRecord<Long, GenericRecord> =
                    ProducerRecord(
                        KafkaProperties.TOPIC,
                        index,
                        avroRecord,
                    )
                logger.info(
                    """Sending an Avro record: 
                        |key=${record.key()}, value=${record.value()}
                    """.trimMargin(),
                )

                producer.send(
                    record,
                    Callback { metadata, exception ->
                        if (exception == null) {
                            val elapsedTime = System.currentTimeMillis() - time
                            logger.info(
                                """The Avro record metadata: 
                        |key=${record.key()} partition=${metadata.partition()} 
                        |offset=${metadata.offset()} time=$elapsedTime
                                """.trimMargin(),
                            )
                        } else {
                            logger.error("Cannot produce a message! ", exception)
                        }
                    },
                )
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

    class KotlinKafkaAvroWithRegistryProducerBuilder {
        private var numberOfMessages: Long = 0

        fun withNumberOfMessages(numberOfMessages: Long) = apply { this.numberOfMessages = numberOfMessages }

        fun build(): KotlinKafkaAvroWithRegistryProducer {
            val props = Properties()
            props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaProperties.BOOTSTRAP_SERVERS
            props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java.name
            props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java.name
            // CLIENT_ID_CONFIG: ID of the producer so that the broker can determine the source of the request.
            props[ProducerConfig.CLIENT_ID_CONFIG] = KafkaProperties.PRODUCER_AVRO_CLIENT_ID

            // Avro serializer property - the address of Schema Registry
            props["schema.registry.url"] = KafkaProperties.AVRO_SCHEMA_REGISTRY

            return KotlinKafkaAvroWithRegistryProducer(KafkaProducer(props), numberOfMessages)
        }
    }
}
