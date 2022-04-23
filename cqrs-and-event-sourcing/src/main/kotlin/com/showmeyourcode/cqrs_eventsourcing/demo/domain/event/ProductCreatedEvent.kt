package com.showmeyourcode.cqrs_eventsourcing.demo.domain.event

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.ProductID

data class ProductCreatedEvent(
    val productID: ProductID,
    val availability: Int,
    val name: String
) : Event {
    override fun getDomainEntityId(): String {
        return productID.toString()
    }
}
