package com.example.outbound.dto

import com.example.inbound.domain.Source
import java.time.LocalDateTime

class InStockBookDto(
    var title: String,
    var description: String,
    var author: String,
    var publisher: String,
    var isbn: Long,
    var publicationDate: LocalDateTime,
    var source: Source,
) {
}