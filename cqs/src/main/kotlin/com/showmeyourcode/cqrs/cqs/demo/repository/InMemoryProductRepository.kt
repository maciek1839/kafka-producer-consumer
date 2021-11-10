package com.showmeyourcode.cqrs.cqs.demo.repository

import com.showmeyourcode.cqrs.cqs.demo.domain.Product
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Component
class InMemoryProductRepository {
    private val log: Logger = LoggerFactory.getLogger(this.javaClass)
    private val products: MutableMap<UUID, Product> = ConcurrentHashMap<UUID, Product>()

    fun addProduct(product: Product): UUID{
        val id = UUID.randomUUID()
        log.info("A new UUID generated: $id")
        products[id]=Product(id,product.name, product.availability)
        return id
    }

    fun updateAvailability(id: UUID, newAvailability: Int) {
        val p = products[id]
        if (p?.id != null){
            products[id]= Product(p.id, p.name,p.availability)
        }
    }

    fun getProduct(productId: UUID): Product? {
        return products[productId]
    }

    fun getAll(): MutableCollection<Product> {
        return products.values
    }
}
