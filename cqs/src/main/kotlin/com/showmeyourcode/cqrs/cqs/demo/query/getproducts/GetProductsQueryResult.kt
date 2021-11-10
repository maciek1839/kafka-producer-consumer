package com.showmeyourcode.cqrs.cqs.demo.query.getproducts

import com.showmeyourcode.cqrs.cqs.demo.domain.Product

class GetProductsQueryResult(val products: Collection<Product>)
