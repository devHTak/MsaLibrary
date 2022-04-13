package com.example.outbound.dto

import com.example.inbound.domain.BookStatus

class BookStockEventDto(
    var bookId: String,
    var bookStock: BookStatus
) {
}