package com.showmeyourcode.cqrs_eventsourcing.demo.repository.query

import com.showmeyourcode.cqrs_eventsourcing.demo.domain.query.ProductQ
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ProductQueryRepository : JpaRepository<ProductQ, UUID>
