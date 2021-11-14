package com.showmeyourcode.cqrs.demo.repository.command

import com.showmeyourcode.cqrs.demo.domain.command.ProductC
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface ProductCommandRepository : MongoRepository<ProductC, UUID>
