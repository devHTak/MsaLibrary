package com.example.outbound.dto

import com.example.inbound.domain.BookStatus
import com.example.inbound.domain.Classificattion
import com.example.inbound.domain.ISBN
import com.example.inbound.domain.Location
import java.time.LocalDateTime

class BookChanged(
    var title: String,
    var description: String,
    var author: String,
    var publicationDate: LocalDateTime?,
    var classificattion: Classificattion?,
    var rented: Boolean?,
    var eventType: BookChangeStatus,
    var rentCnt: Long,
    var bookId: String
) {
    constructor(bookId:String, eventType: BookChangeStatus) :
            this("", "", "", null, null, false,
                eventType,0L, bookId)
}