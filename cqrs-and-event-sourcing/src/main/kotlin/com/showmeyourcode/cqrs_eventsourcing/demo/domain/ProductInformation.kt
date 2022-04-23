package com.showmeyourcode.cqrs_eventsourcing.demo.domain

import java.time.LocalDateTime

data class ProductInformation(
    val name: String,
    var availability: Int,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    var modifiedDate: LocalDateTime = LocalDateTime.now()
)
