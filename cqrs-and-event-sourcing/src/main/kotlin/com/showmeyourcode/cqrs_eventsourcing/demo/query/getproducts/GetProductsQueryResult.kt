package com.showmeyourcode.cqrs_eventsourcing.demo.query.getproducts

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.query.ProductQ

class GetProductsQueryResult(val products: Collection<ProductQ>)
