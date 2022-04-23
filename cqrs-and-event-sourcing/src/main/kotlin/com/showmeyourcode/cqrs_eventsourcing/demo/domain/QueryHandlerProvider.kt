package com.showmeyourcode.cqrs_eventsourcing.demo.domain

import com.showmeyourcode.cqrs_eventsourcing.demo.query.getproductavailability.GetProductAvailabilityHandler
import com.showmeyourcode.cqrs_eventsourcing.demo.query.getproductavailability.GetProductAvailabilityQuery
import com.showmeyourcode.cqrs_eventsourcing.demo.query.getproductavailability.GetProductAvailabilityQueryResult
import com.showmeyourcode.cqrs_eventsourcing.demo.query.getproducts.GetProductsHandler
import com.showmeyourcode.cqrs_eventsourcing.demo.query.getproducts.GetProductsQuery
import com.showmeyourcode.cqrs_eventsourcing.demo.query.getproducts.GetProductsQueryResult
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class QueryHandlerProvider constructor(private val applicationContext: ApplicationContext) {

    fun getProductAvailability(query: GetProductAvailabilityQuery): GetProductAvailabilityQueryResult {
        return applicationContext.getBean(GetProductAvailabilityHandler::class.java).handle(query)
    }

    fun getProducts(query: GetProductsQuery): GetProductsQueryResult {
        return applicationContext.getBean(GetProductsHandler::class.java).handle(query)
    }
}

