package com.example.inbound.domain

import com.example.inbound.except.RentalUnavailableException
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class Rental(
    @Id @GeneratedValue
    var id: Long,
    var rentalId: String,
    var userId: String,
    var bookId: String,
    @Enumerated(value = EnumType.STRING)
    var status: RentalStatus,
    var lateFee: Long,

    @OneToMany(mappedBy ="rental", cascade = arrayOf(CascadeType.ALL), orphanRemoval = false)
    var rentedItems: MutableList<RentedItem>,

    @OneToMany(mappedBy ="rental", cascade = arrayOf(CascadeType.ALL), orphanRemoval = false)
    var overdueItems: MutableList<OverdueItem>,

    @OneToMany(mappedBy ="rental", cascade = arrayOf(CascadeType.ALL), orphanRemoval = false)
    var returnedItems: MutableList<ReturnedItem>
    ) {
    constructor(): this(0L, "","", "", RentalStatus.RENT_AVAILABLE, 0L,
        mutableListOf<RentedItem>(), mutableListOf<OverdueItem>(), mutableListOf<ReturnedItem>())

    companion object {
        fun createRental(userId: String): Rental {
            val rental = Rental()
            rental.userId = userId
            rental.rentalId = UUID.randomUUID().toString()

            return rental
        }
    }

    fun checkRentalAvailable(): Boolean {
        if(RentalStatus.RENT_UNAVAILABLE.equals(this.status) || this.lateFee != 0L) {
            throw RentalUnavailableException("연체 상태입니다. 연체료를 정산 후 도서 대출이 가능합니다.")
        }
        if(this.rentedItems.size >= 5) {
            throw RentalUnavailableException("대출 가능한 도서의 수는 총 5권입니다.")
        }
        return true
    }

    fun rentBook(bookId: String, bookTitle: String): RentedItem {
        val rentedItem = RentedItem.createRentedItem(bookId, bookTitle, LocalDateTime.now())
        this.addRentedItem(rentedItem)
        return rentedItem
    }

    fun returnBook(bookId: String): ReturnedItem {
        val rentedItem = rentedItems.stream().filter { rentedItem -> rentedItem.bookId.equals(bookId) }
            .findFirst().orElseThrow{ throw IllegalArgumentException("없는 ID입니다.")}

        var returnedItem = ReturnedItem.createReturnedItem(rentedItem.bookId, rentedItem.bookTitle, LocalDateTime.now())
        this.addReturnedItem(returnedItem)
        this.removeRentedItem(rentedItem)
        return returnedItem
    }

    fun overdueBook(bookId: String): OverdueItem {
        val rentedItem = rentedItems.stream().filter{rentedItem -> rentedItem.bookId.equals(bookId)}
            .findFirst().orElseThrow{-> throw IllegalArgumentException("없는 ID입니다.")}

        val overdueItem = OverdueItem.createOverdueItem(rentedItem.bookId, rentedItem.bookTitle, LocalDateTime.now())
        this.addOverdueItem(overdueItem)
        this.removeRentedItem(rentedItem)
        return overdueItem
    }

    fun returnedOverdueBook(bookId: String): OverdueItem {
        val overdueItem = overdueItems.stream().filter{overdueItem -> overdueItem.bookId.equals(bookId)}
            .findFirst().orElseThrow{-> throw IllegalArgumentException("없는 ID입니다.")}

        this.removeOverdueItem(overdueItem)
        return overdueItem
    }

    fun addRentedItem(rentedItem: RentedItem) {
        if(this.rentedItems == null) {
            rentedItems = mutableListOf()
        }

        rentedItems.add(rentedItem)
        rentedItem.rental = this
    }

    fun removeRentedItem(rentedItem: RentedItem) {
        if(this.rentedItems == null) {
            rentedItems = mutableListOf()
        }
        rentedItems.remove(rentedItem)
        rentedItem.rental = null
    }

    fun addReturnedItem(returnedItem: ReturnedItem) {
        if(this.returnedItems == null) {
            returnedItems = mutableListOf()
        }
        returnedItems.add(returnedItem)
        returnedItem.rental = this
    }

    fun removeReturnedItem(returnedItem: ReturnedItem) {
        if(this.returnedItems == null) {
            returnedItems = mutableListOf()
        }
        returnedItems.remove(returnedItem)
        returnedItem.rental = null
    }

    fun addOverdueItem(overdueItem: OverdueItem) {
        if(this.overdueItems == null) {
            overdueItems = mutableListOf()
        }
        overdueItems.add(overdueItem)
        overdueItem.rental = this
    }

    fun removeOverdueItem(overdueItem: OverdueItem) {
        if(this.overdueItems == null) {
            overdueItems = mutableListOf()
        }
        overdueItems.remove(overdueItem)
        overdueItem.rental = null
    }
}