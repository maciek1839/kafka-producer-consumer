package com.showmeyourcode.cqrs.cqs.demo.query.getproducts

import com.showmeyourcode.cqrs.cqs.demo.infra.Query

class GetProductsQuery(val listSize: Int = 10) : Query<GetProductsQueryResult>
