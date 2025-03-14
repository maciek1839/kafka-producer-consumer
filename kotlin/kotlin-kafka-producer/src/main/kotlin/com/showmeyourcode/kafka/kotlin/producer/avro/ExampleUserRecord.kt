package com.showmeyourcode.kafka.kotlin.producer.avro

import com.github.avrokotlin.avro4k.AvroDoc
import kotlinx.serialization.Serializable

@Serializable()
@AvroDoc("An example of Apache Avro schema")
data class ExampleUserRecord(
    val name: String,
    val age: Long,
    val phoneNumber: String? = null,
)
