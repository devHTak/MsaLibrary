package com.example.outbound.dto

import com.example.inbound.domain.RentalStatus

class BookStatusEventDto(
    var bookId: String,
    var bookStatus: BookStatus
) {

}