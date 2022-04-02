package com.example.outbound.exception

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import java.lang.IllegalArgumentException
import java.lang.RuntimeException

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentExceptionHandler(
        runtimeException: RuntimeException,
        request:WebRequest
    ):ResponseEntity<String> {

        return ResponseEntity.badRequest().body(runtimeException.message)
    }
}