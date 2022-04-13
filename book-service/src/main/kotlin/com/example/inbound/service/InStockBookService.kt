package com.example.inbound.service

import com.example.inbound.domain.InStockBook
import com.example.inbound.repository.InStockBookRepository
import com.example.outbound.dto.BookDto
import com.example.outbound.dto.InStockBookDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class InStockBookService(
    private val inStockBookRepository: InStockBookRepository
) {
    fun findByBookId(bookId: String):InStockBook {
        val inStockBook = inStockBookRepository.findByBookId(bookId)
            .orElseThrow { IllegalArgumentException() }

        return inStockBook
    }

    fun save(inStockBookDto: InStockBookDto): BookDto {
        val inStockBook = InStockBook.createByDto(inStockBookDto)
        val returnInStockBook = inStockBookRepository.save(inStockBook)
        return BookDto(returnInStockBook.bookId, returnInStockBook.title)
    }

    fun update(bookId: String, inStockBookDto: InStockBookDto): BookDto {
        val inStockBook = inStockBookRepository.findByBookId(bookId)
            .orElseThrow { IllegalArgumentException() }
        inStockBook.update(inStockBookDto)

        return BookDto(inStockBook.bookId, inStockBook.title)
    }

    fun deleteByBookId(bookId: String){
        val inStockBook = inStockBookRepository.findByBookId(bookId)
            .orElseThrow { IllegalArgumentException() }
        inStockBookRepository.delete(inStockBook)
    }

    fun delete(inStockBook:InStockBook){
        inStockBookRepository.delete(inStockBook)
    }
}