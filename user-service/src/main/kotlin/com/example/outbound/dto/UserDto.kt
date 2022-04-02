package com.example.outbound.dto

import com.example.inbound.domain.User

class UserDto(
    var username: String,
    var userId: String,
    var email: String,
    var point: Long
) {
    fun convertByUser(user: User) {
        this.username = user.username
        this.userId = user.userId
        this.email = user.email
        this.point = user.point
    }

    constructor(): this("", "", "", 0L)
}