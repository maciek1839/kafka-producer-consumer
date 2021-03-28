package com.showmeyourcode.kafka.kafka_publisher

import com.github.avrokotlin.avro4k.Avro
import com.showmeyourcode.kafka.common.KafkaProperties
import com.showmeyourcode.kafka.kafka_publisher.avro.ExampleUserRecord
import io.confluent.kafka.serializers.KafkaAvroSerializer
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class ExampleKafkaProducerAvro {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ExampleKafkaProducerAvro::class.java);

        private fun getProperties(): Properties {
            val props = Properties()
            props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaProperties.BOOTSTRAP_SERVERS
            props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java.name
            props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = KafkaAvroSerializer::class.java.name
            // CLIENT_ID_CONFIG: Id of the producer so that the broker can determine the source of the request.
            props[ProducerConfig.CLIENT_ID_CONFIG] = "ExampleKafkaProducerAvro"

            // Avro serializer property - the address of Schema Registry
            props["schema.registry.url"]= "http://localhost:8081";

            return props
        }

        private fun createProducer(): Producer<Long, GenericRecord> {
            logger.info("Creating an Avro Kafka producer...")
            return KafkaProducer(getProperties())
        }

        // Reference: https://docs.confluent.io/platform/6.0.0/schema-registry/serdes-develop/serdes-avro.html
        fun runProducer(sendMessageCount: Int) {
            logger.info("Starting an Avro producer...")
            val producer: Producer<Long, GenericRecord> = createProducer()
            val time = System.currentTimeMillis()
            try {
                val schema: Schema = Avro.default.schema(ExampleUserRecord.serializer())
                logger.info("Generated schema: ${schema.toString(true)}")
                for (index in time until time + sendMessageCount) {
                    val dataRecord = ExampleUserRecord("name",index)
                    val avroRecord: GenericRecord = GenericData.Record(schema)
                    avroRecord.put("name", dataRecord.name)
                    avroRecord.put("age", dataRecord.age)
                    avroRecord.put("phoneNumber", dataRecord.phoneNumber)

                    val record: ProducerRecord<Long, GenericRecord> = ProducerRecord(
                        KafkaProperties.TOPIC,
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
