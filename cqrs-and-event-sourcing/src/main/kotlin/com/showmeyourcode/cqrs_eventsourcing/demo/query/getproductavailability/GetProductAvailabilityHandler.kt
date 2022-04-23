package com.showmeyourcode.cqrs_eventsourcing.demo.query.getproductavailability

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.Product
import com.showmeyourcode.cqrs_eventsourcing.demo.domain.QueryHandler
import com.showmeyourcode.cqrs_eventsourcing.demo.infra.eventbus.EventBus
import com.showmeyourcode.cqrs_eventsourcing.demo.repository.eventstore.EventStore
import com.showmeyourcode.cqrs_eventsourcing.demo.repository.query.ProductQueryRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class GetProductAvailabilityHandler(private val repository: ProductQueryRepository) :
    QueryHandler<GetProductAvailabilityQueryResult, GetProductAvailabilityQuery> {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun handle(query: GetProductAvailabilityQuery): GetProductAvailabilityQueryResult {
        log.info("Handling 'GetProductAvailability' command...")
        val product = repository.findById(query.productId)
        if(product.isEmpty){
            throw Exception("The product ${query.productId} was not found!")
        }
        return GetProductAvailabilityQueryResult(product.get().availability)
    }
}

