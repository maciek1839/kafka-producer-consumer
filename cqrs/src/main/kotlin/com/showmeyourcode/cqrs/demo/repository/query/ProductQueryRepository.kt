package com.showmeyourcode.cqrs.demo.repository.query

import com.showmeyourcode.cqrs.demo.domain.query.ProductQ
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProductQueryRepository : JpaRepository<ProductQ, UUID>
