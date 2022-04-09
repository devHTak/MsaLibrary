package com.example.outbound.adaptor.producer

import com.example.outbound.dto.BookStatusEventDto
import com.example.outbound.dto.BookStockEventDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class RentalProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    @Value("api.topic.book.stock-event") private val topicStockEvent: String,
    @Value("api.topic.book.status-event") private val topicStatusEvent: String
) {

    fun sendForStockEvent(bookStockEventDto: BookStockEventDto) {
        val data = objectMapper.writeValueAsString(bookStockEventDto);
        kafkaTemplate.send(topicStockEvent, data)
    }

    fun sendForStatusEvent(bookStatusEventDto: BookStatusEventDto) {
        val data = objectMapper.writeValueAsString(bookStatusEventDto);
        kafkaTemplate.send(topicStatusEvent, data)
    }
}