package com.example.inbound.domain

import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class OverdueItem(
    @Id @GeneratedValue
    var id: Long,

    var bookId: String,

    var bookTitle: String,

    var overdueDate: LocalDateTime,

    @ManyToOne
    var rental: Rental?
) {
    constructor(): this(0L, "", "", LocalDateTime.now(), null)
    companion object {
        fun createOverdueItem(bookId: String, bookTitle: String, now:LocalDateTime): OverdueItem {
            val overdueItem = OverdueItem()
            overdueItem.bookId = bookId
            overdueItem.bookTitle = bookTitle
            overdueItem.overdueDate = now

            return overdueItem
        }
    }
}