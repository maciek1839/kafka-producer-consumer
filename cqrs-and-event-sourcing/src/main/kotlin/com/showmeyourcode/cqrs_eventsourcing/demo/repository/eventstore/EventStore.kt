package com.showmeyourcode.cqrs_eventsourcing.demo.repository.eventstore

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.ProductID
import com.showmeyourcode.cqrs_eventsourcing.demo.domain.event.Event

interface EventStore {
    fun save(event: Event)

    fun saveAll(events: List<Event>) {
        events.forEach(this::save)
    }

    fun allFor(productNumber: ProductID): List<Event>

    fun exists(productNumber: ProductID): Boolean
}

