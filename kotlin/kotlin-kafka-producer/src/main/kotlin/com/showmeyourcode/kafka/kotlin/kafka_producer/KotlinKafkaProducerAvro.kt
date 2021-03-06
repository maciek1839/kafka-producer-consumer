package com.showmeyourcode.kafka.kotlin.kafka_producer

import com.github.avrokotlin.avro4k.Avro
import com.showmeyourcode.kafka.kotlin.common.KotlinKafkaProperties
import com.showmeyourcode.kafka.kotlin.kafka_producer.avro.ExampleUserRecord
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class KotlinKafkaProducerAvro {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(KotlinKafkaProducerAvro::class.java);

        private fun getProperties(): Properties {
            val props = Properties()
            props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = KotlinKafkaProperties.BOOTSTRAP_SERVERS
            props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java.name
            props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java.name
            // CLIENT_ID_CONFIG: Id of the producer so that the broker can determine the source of the request.
            props[ProducerConfig.CLIENT_ID_CONFIG] = KotlinKafkaProperties.PRODUCER_AVRO_CLIENT_ID

            // Avro serializer property - the address of Schema Registry
            props["schema.registry.url"] = KotlinKafkaProperties.AVRO_SCHEMA_REGISTRY

            return props
        }

        private fun createProducer(): Producer<Long, GenericRecord> {
            logger.info("Creating an Avro Kotlin producer...")
            return KafkaProducer(getProperties())
        }

        // Reference: https://docs.confluent.io/platform/6.0.0/schema-registry/serdes-develop/serdes-avro.html
        fun runProducer(sendMessageCount: Int) {
            logger.info("Starting an Avro Kotlin producer...")
            val producer: Producer<Long, GenericRecord> = createProducer()
            val time = System.currentTimeMillis()
            try {
                val schema: Schema = Avro.default.schema(ExampleUserRecord.serializer())
                logger.info("Generated schema: ${schema.toString(true)}")
                for (index in time until time + sendMessageCount) {
                    val dataRecord = ExampleUserRecord("name", index)
                    val avroRecord: GenericRecord = GenericData.Record(schema)
                    avroRecord.put("name", dataRecord.name)
                    avroRecord.put("age", dataRecord.age)
                    avroRecord.put("phoneNumber", dataRecord.phoneNumber)

                    val record: ProducerRecord<Long, GenericRecord> = ProducerRecord(
                        KotlinKafkaProperties.TOPIC,
                        index,
                        avroRecord
                    )
                    val metadata: RecordMetadata = producer.send(record).get()
                    val elapsedTime = System.currentTimeMillis() - time
                    logger.info("Sending an Avro record: ${record.key()}(key=${record.value()} value=${metadata.partition()})")
                    logger.info("The Avro record metadata: partition=${metadata.partition()}, offset=${metadata.offset()}) time=${elapsedTime}")
                }
            } finally {
                producer.flush()
                producer.close()
            }
        }
    }
}
