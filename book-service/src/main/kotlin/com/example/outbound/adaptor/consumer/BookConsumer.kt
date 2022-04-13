package com.example.outbound.adaptor.consumer

import com.example.inbound.service.BookService
import com.example.outbound.dto.BookChanged
import com.example.outbound.dto.BookStockEventDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class BookConsumer(
    private val objectMapper: ObjectMapper,
    private val bookService: BookService
) {

    @KafkaListener(topics = arrayOf("TOPIC_BOOK"), groupId = "consumer_group")
    fun consume(kafkaMessage: String) {
        val bookStockEventDto = objectMapper.readValue(kafkaMessage, BookStockEventDto::class.java)
        bookService.processChangeBookState(bookStockEventDto.bookId, bookStockEventDto.bookStock)
    }
}