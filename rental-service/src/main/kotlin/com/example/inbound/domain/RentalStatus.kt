package com.example.inbound.domain

enum class RentalStatus(
    var code: Int,
    var statusName: String,
    var statusDescription: String
) {
    RENT_AVAILABLE(0, "대출가능", "대출가능상태"),
    RENT_UNAVAILABLE(1, "대출불가", "대출불가능상태")
}
