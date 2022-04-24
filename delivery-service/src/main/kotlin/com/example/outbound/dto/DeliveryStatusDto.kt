package com.example.outbound.dto

import com.example.inbound.domain.DeliveryStatus
import java.time.LocalDateTime

class DeliveryStatusDto(
    var deliveryId: String,
    var userId: String,
    var rentalId: String,
    var deliveryStatus: DeliveryStatus,
    var createdAt: LocalDateTime,
    var startDate: LocalDateTime?,
    var completeDate: LocalDateTime?
) {
}