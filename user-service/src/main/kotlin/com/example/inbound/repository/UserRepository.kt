package com.example.inbound.repository

import com.example.inbound.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, Long> {
    fun findByUserId(userId: String): Optional<User>
    fun findByEmail(email: String): Optional<User>
}