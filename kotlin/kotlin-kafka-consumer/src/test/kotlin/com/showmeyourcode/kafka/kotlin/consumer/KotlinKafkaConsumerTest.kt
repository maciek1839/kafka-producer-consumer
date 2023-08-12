package com.showmeyourcode.kafka.kotlin.consumer

import com.showmeyourcode.kafka.kotlin.common.KafkaProperties
import org.apache.kafka.clients.consumer.*
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.errors.InvalidGroupIdException
import org.apache.kafka.common.serialization.StringDeserializer
import org.assertj.core.api.Assertions
import org.assertj.core.api.ThrowableAssert
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.time.Duration
import java.util.*

class KotlinKafkaConsumerTest {

    @Test
    fun shouldBuildValidConsumer() {
        val kotlinKafkaConsumer = KotlinKafkaConsumer.KotlinKafkaConsumerBuilder()
            .withNumberOfMessages(10L)
            .build()
        Assertions.assertThat(kotlinKafkaConsumer.numberOfMessagesToConsume).isEqualTo(10L)
    }

    @Test
    fun shouldConsumeKafkaMessagesWhenConfigurationIsValid() {
        val kafkaConsumer = Mockito.mock(
            MockConsumer<String, String>(OffsetResetStrategy.EARLIEST)::class.java,
        )
        Mockito.`when`(kafkaConsumer.poll(ArgumentMatchers.any<Duration>())).thenReturn(
            ConsumerRecords(
                mapOf(
                    Pair(
                        TopicPartition("topic1", 0),
                        listOf(ConsumerRecord("topic", 0, 123L, "key", "value")),
                    ),
                ),
            ),
        )

        KotlinKafkaConsumer(kafkaConsumer, 2L).consume()

        Mockito.verify(kafkaConsumer, Mockito.times(2)).poll(ArgumentMatchers.any<Duration>())
    }

    @Test
    fun shouldThrowExceptionWhenCannotFetchMessages() {
        val throwable: InvalidGroupIdException =
            ThrowableAssert.catchThrowableOfType(
                {
                    val props = Properties()
                    props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = KafkaProperties.BOOTSTRAP_SERVERS
                    props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
                    props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name

                    KotlinKafkaConsumer(
                        KafkaConsumer(
                            props,
                        ),
                        5L,
                    ).consume()
                },
                InvalidGroupIdException::class.java,
            )
        Assertions.assertThat(throwable)
            .hasMessage(
                "To use the group management or offset commit APIs, " +
                    "you must provide a valid group.id in the consumer configuration.",
            )
    }
}
