package com.showmeyourcode.cqrs_eventsourcing.demo.query.getproductavailability

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.Query
import java.util.*


class GetProductAvailabilityQuery(val productId: UUID) :
    Query<GetProductAvailabilityQueryResult>
