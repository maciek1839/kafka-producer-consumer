package com.showmeyourcode.cqrs_eventsourcing.demo.query.getproducts

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.Query

class GetProductsQuery(val listSize: Int = 10) : Query<GetProductsQueryResult>
