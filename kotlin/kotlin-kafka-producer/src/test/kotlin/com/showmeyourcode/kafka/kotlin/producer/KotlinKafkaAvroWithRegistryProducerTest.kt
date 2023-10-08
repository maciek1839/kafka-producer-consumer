package com.showmeyourcode.kafka.kotlin.producer

import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.TopicPartition
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.concurrent.CompletableFuture
import kotlin.test.assertFailsWith

class KotlinKafkaAvroWithRegistryProducerTest {
    @Test
    fun shouldBuildValidAvroProducer() {
        val producer: KotlinKafkaAvroWithRegistryProducer =
            KotlinKafkaAvroWithRegistryProducer.KotlinKafkaAvroWithRegistryProducerBuilder()
                .withNumberOfMessages(10L)
                .build()
        Assertions.assertThat(producer.numberOfMessages).isEqualTo(10L)
    }

    @Test
    fun shouldProduceKafkaMessagesWhenAvroFileConfigurationIsValid() {
        val kafkaProducer =
            mock<Producer<Long, GenericRecord>> {
                on { send(any(), any()) } doReturn
                    CompletableFuture.completedFuture(
                        RecordMetadata(
                            TopicPartition("topic1", 0),
                            1,
                            1,
                            1,
                            Long.MIN_VALUE,
                            1,
                            1,
                        ),
                    )
            }
        KotlinKafkaAvroWithRegistryProducer(kafkaProducer, 2L).produce()

        verify(kafkaProducer, times(2)).send(any(), any())
    }

    @Test
    fun shouldThrowExceptionWhenCannotPublishMessages() {
        val producer =
            mock<KafkaProducer<Long, GenericRecord>> {
                on { send(any(), any()) } doThrow RuntimeException("Buum!")
            }

        assertFailsWith<RuntimeException>(
            block = {
                KotlinKafkaAvroWithRegistryProducer(producer, 2L).produce()
            },
        )
    }
}
