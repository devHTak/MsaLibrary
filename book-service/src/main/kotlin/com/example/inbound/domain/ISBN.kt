package com.example.inbound.domain

import java.lang.IllegalArgumentException
import javax.persistence.Embeddable

@Embeddable
class ISBN(
    var value: Long
) {
    private val isbn: Long? = null

    init {
        if (isValidISBN(isbn!!)) {
            this.value = isbn
        }
        throw IllegalArgumentException()
    }

    private fun isValidISBN(isbn: Long): Boolean {
        return if (isbn >= 1000000000000L) false else getCheckSum(isbn) % 10 == 0
    }

    private fun getCheckSum(isbn: Long): Int {
        var isbn = isbn
        var sum = 0
        while (isbn / 10 > 0) {
            sum += (isbn % 10).toInt()
            isbn /= 10
        }
        return sum
    }
}