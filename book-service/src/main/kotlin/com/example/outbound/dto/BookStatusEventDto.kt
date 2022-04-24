package com.example.outbound.dto

import com.example.inbound.domain.BookStatus

class BookStatusEventDto(
    var bookId: String,
    var bookStatus: BookStatus
) {
}