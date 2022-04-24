package com.example.inbound.repository

import com.example.inbound.domain.Delivery
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface DeliveryRepository: JpaRepository<Delivery, Long> {
    fun findByDeliveryId(deliveryId: String): Optional<Delivery>
    fun findByUserId(userId: String): List<Delivery>
    fun findByRentalId(rentalId: String): List<Delivery>
}