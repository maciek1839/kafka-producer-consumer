package com.showmeyourcode.cqrs_eventsourcing.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CqrsAndEventSourcingDemoApplication

fun main(args: Array<String>) {
	runApplication<CqrsAndEventSourcingDemoApplication>(*args)
}
