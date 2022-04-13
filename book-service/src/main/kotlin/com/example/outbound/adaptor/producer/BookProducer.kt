package com.example.outbound.adaptor.producer

import com.example.outbound.dto.BookChanged
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class BookProducer(
    private val objectMapper: ObjectMapper,
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    @Value("topic.catalog")
    private val TOPIC_CATALOG = "TOPIC_CATALOG";

    fun sendBookEvent(bookChanged: BookChanged) {
        val message = objectMapper.writeValueAsString(bookChanged)
        kafkaTemplate.send(ProducerRecord(TOPIC_CATALOG, message)).get()
    }
}