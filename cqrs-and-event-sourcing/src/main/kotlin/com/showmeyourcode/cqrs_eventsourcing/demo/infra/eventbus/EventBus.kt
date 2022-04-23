package com.showmeyourcode.cqrs_eventsourcing.demo.infra.eventbus

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.event.Event

interface EventBus {
    fun send(event: Event)

    fun sendAll(events: List<Event>) {
        events.forEach(this::send)
    }
}
