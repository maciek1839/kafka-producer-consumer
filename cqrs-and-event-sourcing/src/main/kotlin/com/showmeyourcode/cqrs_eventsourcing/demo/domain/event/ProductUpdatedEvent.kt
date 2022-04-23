package com.showmeyourcode.cqrs_eventsourcing.demo.domain.event

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.ProductID

data class ProductUpdatedEvent(
    val productID: ProductID,
    val availability: Int
) : Event {
    override fun getDomainEntityId(): String {
        return productID.toString()
    }
}
