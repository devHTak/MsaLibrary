package com.example.outbound.dto

import com.example.inbound.domain.BookStatus
import com.example.inbound.domain.Classificattion
import com.example.inbound.domain.Location
import java.time.LocalDateTime

class UpdateBookDto(
    var title: String?,
    var description: String?,
    var author: String?,
    var publisher: String?,
    var isbn: Long?,
    var publicationDate: LocalDateTime?,
    var classificattion: Classificattion?,
    var bookStatus: BookStatus?,
    var location: Location?,
) {
}