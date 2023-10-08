package com.showmeyourcode.kafka.kotlin.producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.TopicPartition
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
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
        val kafkaProducer =
            mock<Producer<Long, String>> {
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

        KotlinKafkaProducer(kafkaProducer, 2L).produce()

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
