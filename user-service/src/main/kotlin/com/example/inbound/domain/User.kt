package com.example.inbound.domain

import com.example.outbound.dto.CalculateType
import com.example.outbound.dto.PointDto
import com.example.outbound.dto.SaveUserDto
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class User(
    @Id @GeneratedValue
    var id: Long,

    var userId: String,
    var username: String,
    var email: String,
    var password: String,

    @Enumerated(EnumType.STRING)
    var authority: Authority,

    var point: Long,

    @Embedded
    var address: Address,

    var createdAt: LocalDateTime,

    var modifiedAt: LocalDateTime,

    var isUse: Boolean
) {
    fun convertBySaveUserDto(saveUserDto: SaveUserDto) {
        this.userId = UUID.randomUUID().toString()
        this.username = saveUserDto.username
        this.email = saveUserDto.email
        this.password = saveUserDto.password
        this.authority = Authority.USER
        this.point = 0
        this.address = Address(saveUserDto.city, saveUserDto.street, saveUserDto.zipCode)
        this.createdAt = LocalDateTime.now()
        this.modifiedAt = LocalDateTime.now()
        this.isUse = true
    }

    fun calcualtePoint(point: PointDto) {
        if(CalculateType.SAVE.equals(point.calculateType)) {
            this.point += point.point
        } else {
            if(this.point - point.point < 0) {
                throw IllegalArgumentException()
            }

            this.point -= point.point
        }
    }

    constructor(): this(0L, "", "", "", "", Authority.USER, 0L, Address(), LocalDateTime.now(), LocalDateTime.now(), true)
}