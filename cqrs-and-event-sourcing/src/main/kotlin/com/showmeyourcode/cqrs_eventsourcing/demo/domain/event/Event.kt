package com.showmeyourcode.cqrs_eventsourcing.demo.domain.event

interface Event {
    fun getDomainEntityId(): String
}
