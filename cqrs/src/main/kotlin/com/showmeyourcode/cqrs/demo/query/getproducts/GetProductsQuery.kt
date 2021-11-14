package com.showmeyourcode.cqrs.demo.query.getproducts

import com.showmeyourcode.cqrs.demo.infra.Query

class GetProductsQuery(val listSize: Int = 10) : Query<GetProductsQueryResult>
