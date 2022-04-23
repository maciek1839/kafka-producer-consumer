package com.showmeyourcode.cqrs_eventsourcing.demo.command.addproduct

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.Product
import com.showmeyourcode.cqrs_eventsourcing.demo.infra.eventbus.EventBus
import com.showmeyourcode.cqrs_eventsourcing.demo.domain.CommandHandler
import com.showmeyourcode.cqrs_eventsourcing.demo.repository.eventstore.EventStore
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Component
class AddProductHandler(
    private val eventStore: EventStore,
    private val eventBus: EventBus
) : CommandHandler<AddProductCommandResult, AddProductCommand> {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun handle(command: AddProductCommand) {
        log.info("Handling 'AddProductCommand' command...")
        val product = Product()
        product.handle(command)
        val events = product.occurredEvents()
        eventBus.sendAll(events)
        eventStore.saveAll(events)
    }
}
