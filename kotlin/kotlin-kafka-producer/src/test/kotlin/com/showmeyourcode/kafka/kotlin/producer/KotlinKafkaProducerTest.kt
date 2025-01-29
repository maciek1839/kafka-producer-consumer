package com.showmeyourcode.kafka.kotlin.producer

import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.TopicPartition
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.*
import java.util.concurrent.CompletableFuture

class KotlinKafkaProducerTest {
    private val resultRecord =
        RecordMetadata(
            TopicPartition("topic1", 0),
            1,
            1,
            1,
            Long.MIN_VALUE,
            1,
            1,
        )

    @Test
    fun shouldBuildValidAvroProducer() {
        val producer: KotlinKafkaProducer =
            KotlinKafkaProducer
                .KotlinKafkaProducerBuilder()
                .withNumberOfMessages(10L)
                .build()
        Assertions.assertThat(producer.numberOfMessages).isEqualTo(10L)
    }

    @Test
    fun shouldProduceKafkaMessagesWhenConfigurationIsValid() {
        val kafkaProducer =
            mock<Producer<Long, String>> {
                on { send(any(), any()) } doAnswer {
                    it.arguments
                    val callback = it.getArgument(1) as Callback
                    callback.onCompletion(resultRecord, null)
                    CompletableFuture.completedFuture(resultRecord)
                }
            }

        KotlinKafkaProducer(kafkaProducer, 2L).produce()

        verify(kafkaProducer, times(2)).send(any(), any())
    }

    @Test
    fun shouldHandleExceptionsGracefullyWhenCannotPublishMessages() {
        val kafkaProducer =
            mock<Producer<Long, String>> {
                on { send(any(), any()) } doAnswer {
                    it.arguments
                    val callback = it.getArgument(1) as Callback
                    callback.onCompletion(resultRecord, RuntimeException("Buum!"))
                    CompletableFuture.completedFuture(resultRecord)
                }
            }

        assertDoesNotThrow {
            KotlinKafkaProducer(kafkaProducer, 2L).produce()
        }
    }
}
