package com.example.outbound.dto

class SaveUserDto(
    val username: String,
    val email: String,
    var password: String,
    val street: String,
    val zipCode: String,
    val city: String
) {


}