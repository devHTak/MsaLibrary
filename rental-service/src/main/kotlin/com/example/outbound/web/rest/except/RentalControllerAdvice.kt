package com.example.outbound.web.rest.except

import com.example.inbound.except.RentalUnavailableException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class RentalControllerAdvice {

    @ExceptionHandler(value = arrayOf(IllegalArgumentException::class))
    fun illegalArgumentExceptionHandler(exception: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.message)
    }

    @ExceptionHandler(value = arrayOf(RentalUnavailableException::class))
    fun rentalUnavailableExceptionhandler(exception: RentalUnavailableException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.message)
    }
}