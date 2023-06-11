package com.showmeyourcode.kafka.kotlin.producer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class KotlinKafkaProducerTest {

    @Test
    fun shouldBuildValidAvroProducer() {
        val producer: KotlinKafkaProducer = KotlinKafkaProducer.KotlinKafkaProducerBuilder()
            .withNumberOfMessages(10L)
            .build()
        Assertions.assertThat(producer.numberOfMessages).isEqualTo(10L)
    }
}
