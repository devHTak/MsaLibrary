package com.example.inbound.repository

import com.example.inbound.domain.RentedItem
import org.springframework.data.jpa.repository.JpaRepository

interface RentedItemRepository: JpaRepository<RentedItem, Long> {
}