package com.example.inbound.repository

import com.example.inbound.domain.ReturnedItem
import org.springframework.data.jpa.repository.JpaRepository

interface ReturnedItemRepository: JpaRepository<ReturnedItem, Long> {
}