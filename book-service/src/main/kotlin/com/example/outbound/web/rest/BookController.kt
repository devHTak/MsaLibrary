package com.example.outbound.web.rest

import com.example.inbound.service.BookService
import com.example.outbound.dto.BookDto
import com.example.outbound.dto.UpdateBookDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class BookController(
    private val bookService: BookService
) {

    @PostMapping("/books/{bookId}")
    fun saveBookByInStockBook(@PathVariable bookId: String): ResponseEntity<BookDto> {
        val bookDto = bookService.saveBook(bookId)

        return ResponseEntity.status(HttpStatus.CREATED).body(bookDto)
    }

    @GetMapping("/books/{bookId}")
    fun findBookInfo(@PathVariable bookId: String): ResponseEntity<BookDto> {
        val bookDto = bookService.findByBookId(bookId)

        return ResponseEntity.ok(bookDto)
    }

    @PutMapping("/books/{bookId}")
    fun updateBookInfo(@PathVariable bookId: String, @RequestBody updateBookDto: UpdateBookDto): ResponseEntity<BookDto> {
        val bookDto = bookService.updateBook(bookId, updateBookDto);
        return ResponseEntity.ok().body(bookDto)
    }

    @DeleteMapping("/books/{bookId}")
    fun deleteBookInfo(@PathVariable bookId: String): ResponseEntity<BookDto> {
        bookService.deleteBook(bookId)
        return ResponseEntity.ok().build()
    }



}