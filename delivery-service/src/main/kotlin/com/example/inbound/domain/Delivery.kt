package com.example.inbound.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Delivery(
    @Id @GeneratedValue var id: Long,

    var deliveryId: String,
    var userId: String,
    var rentalId: String,
    @Enumerated(value = EnumType.STRING) var deliveryStatus: DeliveryStatus,
    var createdAt: LocalDateTime,
    var startDate: LocalDateTime?,
    var completeDate: LocalDateTime?
    ) {
    constructor(deliveryId: String, userId: String, rentalId: String):
            this(0L, deliveryId, userId, rentalId, DeliveryStatus.READY, LocalDateTime.now(), null, null)

    fun changeStatusToStart() {
        if(!this.deliveryStatus.equals(DeliveryStatus.READY))
            throw IllegalArgumentException("Unexpected Delivery Status")

        this.deliveryStatus = DeliveryStatus.START
        this.startDate = LocalDateTime.now()
    }

    fun changeStatusToComplete() {
        if(!this.deliveryStatus.equals(DeliveryStatus.START))
            throw IllegalArgumentException("Unexpected Delivery Status")

        this.deliveryStatus = DeliveryStatus.COMPLETE
        this.completeDate = LocalDateTime.now()
    }
}