package com.showmeyourcode.kafka.kotlin.producer

import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.TopicPartition
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.util.concurrent.CompletableFuture
import kotlin.test.assertFailsWith

class KotlinKafkaProducerTest {
    @Test
    fun shouldBuildValidAvroProducer() {
        val producer: KotlinKafkaProducer =
            KotlinKafkaProducer.KotlinKafkaProducerBuilder()
                .withNumberOfMessages(10L)
                .build()
        Assertions.assertThat(producer.numberOfMessages).isEqualTo(10L)
    }

    @Test
    fun shouldProduceKafkaMessagesWhenConfigurationIsValid() {
        val captor = argumentCaptor<Callback>()
        val resultRecord =
            RecordMetadata(
                TopicPartition("topic1", 0),
                1,
                1,
                1,
                Long.MIN_VALUE,
                1,
                1,
            )
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

        captor.capture().onCompletion(
            RecordMetadata(
                TopicPartition("topic1", 0),
                1,
                1,
                1,
                Long.MIN_VALUE,
                1,
                1,
            ),
            null,
        )

        verify(kafkaProducer, times(2)).send(any(), any())
    }

    @Test
    fun shouldThrowExceptionWhenCannotPublishMessages() {
        val producer =
            mock<KafkaProducer<Long, String>> {
                on { send(any(), any()) } doThrow RuntimeException("Buum!")
            }

        assertFailsWith<RuntimeException>(
            block = {
                KotlinKafkaProducer(producer, 2L).produce()
            },
        )
    }
}
