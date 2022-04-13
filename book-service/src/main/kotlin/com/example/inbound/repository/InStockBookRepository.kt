package com.example.inbound.repository

import com.example.inbound.domain.InStockBook
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface InStockBookRepository: JpaRepository<InStockBook, Long> {
    abstract fun findByBookId(bookId: String): Optional<InStockBook>
}