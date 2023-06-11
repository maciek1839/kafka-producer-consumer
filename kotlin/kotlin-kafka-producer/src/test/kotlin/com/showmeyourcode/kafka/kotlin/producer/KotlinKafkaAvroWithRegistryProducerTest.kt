package com.showmeyourcode.kafka.kotlin.producer

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class KotlinKafkaAvroWithRegistryProducerTest {

    @Test
    fun shouldBuildValidAvroProducer() {
        val producer: KotlinKafkaAvroWithRegistryProducer = KotlinKafkaAvroWithRegistryProducer.KotlinKafkaAvroWithRegistryProducerBuilder()
            .withNumberOfMessages(10L)
            .build()
        Assertions.assertThat(producer.numberOfMessages).isEqualTo(10L)
    }

}
