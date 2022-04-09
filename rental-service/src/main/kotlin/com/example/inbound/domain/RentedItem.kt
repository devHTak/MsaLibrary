package com.example.inbound.domain

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class RentedItem(
    @Id @GeneratedValue
    var id: Long,

    var bookId: String,

    var bookTitle: String,

    var rentedDate: LocalDateTime,

    @ManyToOne
    var rental: Rental?
    ) {
    constructor(): this(0L, "", "", LocalDateTime.now(), null)
    companion object {
        fun createRentedItem(bookId: String, bookTitle: String, now:LocalDateTime):RentedItem {
            val rentedItem = RentedItem()
            rentedItem.bookId = bookId
            rentedItem.bookTitle = bookTitle
            rentedItem.rentedDate = now

            return rentedItem
        }
    }

}
