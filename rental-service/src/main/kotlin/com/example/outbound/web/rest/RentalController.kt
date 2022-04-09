package com.example.outbound.web.rest

import com.example.inbound.service.RentalService
import com.example.outbound.adaptor.client.BookClient
import com.example.outbound.dto.RentalDto
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class RentalController(
    private val rentalService: RentalService,
    private val bookClient: BookClient,
    private val circuitBreakerFactory: Resilience4JCircuitBreakerFactory
) {

    @PostMapping("/rentals/{userId}/rentedItem/{bookId}")
    fun rentalBook(@PathVariable userId:String, @PathVariable bookId: String): ResponseEntity<RentalDto> {
        val circuitBreaker = circuitBreakerFactory.create("circuitBreaker")
        val responseBook = circuitBreaker.run({bookClient.findBookInfo(bookId)}, { throw IllegalArgumentException("없는 ID입니다.")})
        responseBook.body ?: throw IllegalArgumentException("")

        val rentalDto = rentalService.rentalBook(userId, bookId, responseBook.body!!.bookTitle)
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalDto)
    }

    @DeleteMapping("/rentals/{userId}/rentedItem/{bookId}")
    fun returnBook(@PathVariable userId:String, @PathVariable bookId: String): ResponseEntity<RentalDto> {
        val rentalDto = rentalService.returnBook(userId, bookId)

        return ResponseEntity.ok(rentalDto)
    }

    @PostMapping("/rentals/{userId}/overdueItem/{bookId}")
    fun overdueBook(@PathVariable userId:String, @PathVariable bookId: String): ResponseEntity<RentalDto> {

        val rentalDto = rentalService.overdueBook(userId, bookId)
        return ResponseEntity.status(HttpStatus.CREATED).body(rentalDto)
    }

    @DeleteMapping("/rentals/{userId}/overdueItem/{bookId}")
    fun returnOverdueBook(@PathVariable userId:String, @PathVariable bookId: String): ResponseEntity<RentalDto> {
        val rentalDto = rentalService.returnOverdueBook(userId, bookId)

        return ResponseEntity.ok(rentalDto)
    }
}