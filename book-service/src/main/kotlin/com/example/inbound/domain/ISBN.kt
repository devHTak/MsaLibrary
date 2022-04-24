package com.example.inbound.domain

import javax.persistence.Embeddable
import kotlin.IllegalArgumentException

@Embeddable
class ISBN() {
    private var isbn: Long = 0L
    constructor(isbn: Long): this() {
        print("${isbn}")
        if(isValidISBN(isbn)) {
            this.isbn = isbn
            return
        }
        throw IllegalArgumentException()
    }


    private fun isValidISBN(isbn: Long): Boolean {
        return if (isbn >= 1000000000000L) false else getCheckSum(isbn) % 10 == 0L
    }

    private fun getCheckSum(isbn: Long): Long {
        var isbn = isbn
        var sum:Long = 0
        while (isbn > 0) {
            sum += (isbn % 10)
            isbn /= 10
        }
        return sum
    }
}