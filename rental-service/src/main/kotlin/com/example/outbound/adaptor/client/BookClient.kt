package com.example.outbound.adaptor.client

import com.example.outbound.dto.BookDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name="book-service", primary = false)
interface BookClient {

    @GetMapping("/book-service/books/{bookId}")
    fun findBookInfo(@PathVariable bookId: String): ResponseEntity<BookDto>

}