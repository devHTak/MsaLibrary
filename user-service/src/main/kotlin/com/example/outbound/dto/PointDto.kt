package com.example.outbound.dto

class PointDto(
    val point: Int,
    val calculateType: CalculateType
) {
}

enum class CalculateType {
    SAVE, REMOVE
}
