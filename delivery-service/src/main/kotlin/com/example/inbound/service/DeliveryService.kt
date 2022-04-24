package com.example.inbound.service

import com.example.inbound.domain.Delivery
import com.example.inbound.repository.DeliveryRepository
import com.example.outbound.dto.DeliveryDto
import com.example.outbound.dto.DeliveryStatusDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Collectors

@Service
@Transactional
class DeliveryService(
    private val deliveryRepository: DeliveryRepository
) {
    fun saveDeliveryForReady(deliveryDto: DeliveryDto): DeliveryDto {
        val delivery = Delivery(UUID.randomUUID().toString(), deliveryDto.userId, deliveryDto.rentalId)
        val returnDelivery = deliveryRepository.save(delivery);
        return DeliveryDto(returnDelivery.deliveryId, returnDelivery.userId, returnDelivery.rentalId)
    }

    fun updateDeliveryStatusToStart(deliveryDto: DeliveryDto): DeliveryDto {
        print("deliverId: ${deliveryDto.deliveryId}")
        deliveryDto.deliveryId ?: throw IllegalArgumentException("delivery id is null")
        val delivery = deliveryRepository.findByDeliveryId(deliveryDto.deliveryId!!)
            .orElseThrow { IllegalArgumentException("No delivery id") }
        print("deliverId: ${delivery.deliveryId}")
        delivery.changeStatusToStart()

        return DeliveryDto(delivery.deliveryId, delivery.userId, delivery.rentalId)
    }

    fun updateDeliveryStatusToComplete(deliveryDto: DeliveryDto): DeliveryDto {
        deliveryDto.deliveryId ?: throw IllegalArgumentException("delivery id is null")
        val delivery = deliveryRepository.findByDeliveryId(deliveryDto.deliveryId!!)
            .orElseThrow { throw IllegalArgumentException("No delivery id") }
        delivery.changeStatusToComplete()

        return DeliveryDto(delivery.deliveryId, delivery.userId, delivery.rentalId)
    }

    fun retrievesBookStatusByUserId(userId: String): List<DeliveryStatusDto> {
        val deliveryStatuses = deliveryRepository.findByUserId(userId)
            .stream().map { delivery -> DeliveryStatusDto(
                delivery.deliveryId, delivery.userId, delivery.rentalId, delivery.deliveryStatus,
                delivery.createdAt, delivery.startDate, delivery.completeDate
            ) }.collect(Collectors.toList())

        return deliveryStatuses
    }

    fun retrievesBookStatusByRentalId(rentalId: String): List<DeliveryStatusDto> {
        val deliveryStatuses = deliveryRepository.findByRentalId(rentalId)
            .stream().map { delivery -> DeliveryStatusDto(
                delivery.deliveryId, delivery.userId, delivery.rentalId, delivery.deliveryStatus,
                delivery.createdAt, delivery.startDate, delivery.completeDate
            ) }.collect(Collectors.toList())

        return deliveryStatuses
    }
}