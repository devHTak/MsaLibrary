package com.example.inbound.service

import com.example.inbound.domain.Book
import com.example.inbound.domain.BookStatus
import com.example.inbound.repository.BookRepository
import com.example.outbound.adaptor.producer.BookProducer
import com.example.outbound.dto.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BookService(
    private val bookRepository: BookRepository,
    private val inStockBookService: InStockBookService,
    private val bookProducer: BookProducer
) {
    fun findByBookId(bookId: String): BookDto {
        val book = bookRepository.findByBookId(bookId)
            .orElseThrow { IllegalArgumentException() };

        return BookDto(book.bookId, book.title)
    }

    fun saveBook(bookId: String): BookDto {
        val inStockBook = inStockBookService.findByBookId(bookId)

        var book = Book.createByInStockBook(inStockBook)

        var returnBook = bookRepository.save(book)
        inStockBookService.delete(inStockBook)

        sendBookCatalogEvent(BookChangeStatus.NEW_BOOK, returnBook.bookId)
        return BookDto(returnBook.bookId, returnBook.title)
    }

    fun updateBook(bookId: String, updateBookDto: UpdateBookDto): BookDto {
        var book = bookRepository.findByBookId(bookId)
            .orElseThrow { IllegalArgumentException() }
        book.update(updateBookDto)

        sendBookCatalogEvent(BookChangeStatus.UPDATE_BOOK, book.bookId)
        return BookDto(book.bookId, book.title)
    }

    fun deleteBook(bookId: String) {
        var book = bookRepository.findByBookId(bookId)
            .orElseThrow { IllegalArgumentException() }
        bookRepository.delete(book)

        sendBookCatalogEvent(BookChangeStatus.DELETE_BOOK, book.bookId)
    }

    private fun sendBookCatalogEvent(bookChangeStatus: BookChangeStatus, bookId: String) {
        val book = bookRepository.findByBookId(bookId)
            .orElseThrow { IllegalArgumentException() }

        if(BookChangeStatus.NEW_BOOK.equals(bookChangeStatus) || BookChangeStatus.UPDATE_BOOK.equals(bookChangeStatus)) {
            val bookChanged = BookChanged(
                book.title, book.description, book.author, book.publicationDate,
                book.classificattion!!, book.bookStatus!!.equals(BookStatus.AVAILABLE), bookChangeStatus,
                0L, book.bookId)
            bookProducer.sendBookEvent(bookChanged)
        } else {
            val bookChanged = BookChanged(book.bookId, bookChangeStatus)
            bookProducer.sendBookEvent(bookChanged)
        }
    }

    fun processChangeBookState(bookId: String, bookStatus: BookStatus) {
        val book = bookRepository.findByBookId(bookId)
            .orElseThrow { IllegalArgumentException() }
        book.bookStatus = bookStatus
    }
}