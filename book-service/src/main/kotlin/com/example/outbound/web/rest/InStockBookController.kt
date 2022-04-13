package com.example.outbound.web.rest

import com.example.inbound.service.InStockBookService
import com.example.outbound.dto.BookDto
import com.example.outbound.dto.InStockBookDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class InStockBookController(
    private val inStockBookService: InStockBookService
) {

    @PostMapping("/in-stock")
    fun saveInStockBook(@RequestBody inStockBookDto:InStockBookDto): ResponseEntity<BookDto> {
        val bookDto = inStockBookService.save(inStockBookDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookDto)
    }

    @GetMapping("/in-stock/{bookId}")
    fun findByInStockBookInfoByBookId(@PathVariable bookId: String):ResponseEntity<BookDto> {
        val inStockBook = inStockBookService.findByBookId(bookId);
        return ResponseEntity.ok().body(BookDto(inStockBook.bookId, inStockBook.title));
    }

    @PutMapping("/in-stock/{bookId}")
    fun updateByBookId(@PathVariable bookId: String, @RequestBody inStockBookDto: InStockBookDto): ResponseEntity<BookDto> {
        val bookDto = inStockBookService.update(bookId, inStockBookDto)
        return ResponseEntity.ok().body(bookDto)
    }

    @DeleteMapping("/in-stock/{bookId}")
    fun deleteByBookId(@PathVariable bookId: String): ResponseEntity<BookDto> {
        inStockBookService.deleteByBookId(bookId)
        return ResponseEntity.ok().build()
    }
}