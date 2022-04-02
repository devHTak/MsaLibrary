package com.example.outbound.dto

class LoginDto(
    val email: String,
    val password: String
) {
    constructor(): this("", "")
}