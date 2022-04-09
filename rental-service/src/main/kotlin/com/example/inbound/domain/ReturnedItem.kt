package com.example.inbound.domain

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class ReturnedItem(
    @Id @GeneratedValue
    var id: Long,

    var bookId: String,

    var bookTitle: String,

    var returnedDate: LocalDateTime,

    @ManyToOne
    var rental: Rental?
) {
    constructor(): this(0L, "", "", LocalDateTime.now(), null)

    companion object {
        fun createReturnedItem(bookId: String, bookTitle: String, now: LocalDateTime): ReturnedItem {
            val returnedItem = ReturnedItem()
            returnedItem.bookId = bookId
            returnedItem.bookTitle = bookTitle
            returnedItem.returnedDate = now

            return returnedItem
        }
    }
}