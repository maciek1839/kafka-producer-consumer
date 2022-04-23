package com.showmeyourcode.cqrs_eventsourcing.demo.domain

import com.showmeyourcode.cqrs_eventsourcing.demo.command.addproduct.AddProductCommand
import com.showmeyourcode.cqrs_eventsourcing.demo.command.addproduct.AddProductHandler
import com.showmeyourcode.cqrs_eventsourcing.demo.command.changeavailability.ChangeProductAvailabilityCommand
import com.showmeyourcode.cqrs_eventsourcing.demo.command.changeavailability.ChangeProductAvailabilityHandler
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class CommandHandlerProvider constructor(private val applicationContext: ApplicationContext) {

    fun handleAddProduct(cmd: AddProductCommand) {
        return applicationContext.getBean(AddProductHandler::class.java).handle(cmd)
    }

    fun changeProductAvailability(cmd: ChangeProductAvailabilityCommand) {
        return applicationContext.getBean(ChangeProductAvailabilityHandler::class.java).handle(cmd)
    }
}

