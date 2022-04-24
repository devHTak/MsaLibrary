package com.example.outbound.web.rest

import com.example.inbound.service.DeliveryService
import com.example.outbound.dto.DeliveryDto
import com.example.outbound.dto.DeliveryStatusDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class DeliveryController(
    private val deliveryService: DeliveryService
) {

    @PostMapping("/deliveries")
    fun saveDelivery(@RequestBody deliveryDto: DeliveryDto): ResponseEntity<DeliveryDto> {
        var result: DeliveryDto = deliveryService.saveDeliveryForReady(deliveryDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }

    @PutMapping("/deliveries/status/start")
    fun startDelivery(@RequestBody deliveryDto: DeliveryDto): ResponseEntity<DeliveryDto> {
        val result: DeliveryDto = deliveryService.updateDeliveryStatusToStart(deliveryDto)

        return ResponseEntity.ok(result)
    }

    @PutMapping("/deliveries/status/complete")
    fun completeDelivery(@RequestBody deliveryDto: DeliveryDto): ResponseEntity<DeliveryDto> {
        val result: DeliveryDto = deliveryService.updateDeliveryStatusToComplete(deliveryDto)

        return ResponseEntity.ok(result)
    }

    @GetMapping("/deliveries/users/{userId}")
    fun retrievesBookStatusByUserId(@PathVariable userId: String): ResponseEntity<List<DeliveryStatusDto>> {
        val result = deliveryService.retrievesBookStatusByUserId(userId)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/deliveries/rentals/{rentalId}")
    fun retrievesBookStatusByRentalId(@PathVariable rentalId: String): ResponseEntity<List<DeliveryStatusDto>> {
        val result = deliveryService.retrievesBookStatusByRentalId(rentalId)
        return ResponseEntity.ok(result)
    }
}