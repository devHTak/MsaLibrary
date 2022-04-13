package com.example.inbound.repository

import com.example.inbound.domain.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface BookRepository: JpaRepository<Book, Long> {
    fun findByBookId(bookId: String): Optional<Book>
}