package com.showmeyourcode.cqrs.cqs.demo.domain

import java.util.*

class Product {
    val id: UUID?
    val name: String?
    val availability: Int

    constructor(id: UUID, name: String?, availability: Int) {
        this.id = id
        this.name = name
        this.availability = availability
    }

    constructor(name: String?, availability: Int) {
        this.id = null
        this.name = name
        this.availability = availability
    }
}
