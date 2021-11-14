package com.showmeyourcode.cqrs.demo.query.getproducts

import com.showmeyourcode.cqrs.demo.domain.query.ProductQ

class GetProductsQueryResult(val products: Collection<ProductQ>)
