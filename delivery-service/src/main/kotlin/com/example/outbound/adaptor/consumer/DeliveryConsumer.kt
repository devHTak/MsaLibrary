package com.example.outbound.adaptor.consumer

import com.example.inbound.service.DeliveryService
import com.example.outbound.dto.DeliveryDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class DeliveryConsumer(
    private val objectMapper: ObjectMapper,
    private val deliveryService: DeliveryService
) {

    @KafkaListener(topics = arrayOf("DELIVERY-EVENT"), groupId = "consumer_group")
    fun consume(kafkaMessage: String) {
        val deliveryDto  = objectMapper.readValue(kafkaMessage, DeliveryDto::class.java)
        deliveryService.saveDeliveryForReady(deliveryDto)
    }
}