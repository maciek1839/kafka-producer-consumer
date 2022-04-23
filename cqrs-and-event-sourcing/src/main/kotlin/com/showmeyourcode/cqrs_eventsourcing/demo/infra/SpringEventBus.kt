package com.showmeyourcode.cqrs_eventsourcing.demo.infra

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.event.Event
import com.showmeyourcode.cqrs_eventsourcing.demo.infra.eventbus.EventBus
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Component
class SpringEventBus(private val publisher: ApplicationEventPublisher) : EventBus {

    override fun send(event: Event) {
        publisher.publishEvent(event)
    }
}
