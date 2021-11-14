package com.showmeyourcode.cqrs.demo.domain.query

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
data class ProductQ (
    @Id
    val id: UUID = UUID.randomUUID(),
    val name: String?,
    val availability: Int = 0
)
