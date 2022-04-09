package com.example.inbound.repository

import com.example.inbound.domain.OverdueItem
import org.springframework.data.jpa.repository.JpaRepository

interface OverdueItemRepository: JpaRepository<OverdueItem, Long> {
}