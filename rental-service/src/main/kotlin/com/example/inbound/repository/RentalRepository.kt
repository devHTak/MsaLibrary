package com.example.inbound.repository

import com.example.inbound.domain.Rental
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface RentalRepository: JpaRepository<Rental, Long> {
    fun findByUserId(userId: String): Optional<Rental>
}