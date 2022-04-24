package com.example.inbound.service

import com.example.inbound.repository.RentalRepository
import com.example.inbound.repository.RentedItemRepository
import com.example.outbound.adaptor.producer.RentalProducer
import com.example.outbound.dto.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RentalService(
    private val rentalRepository: RentalRepository,
    private val rentedItemRepository: RentedItemRepository,
    private val rentalProducer: RentalProducer
) {

    @Transactional
    fun rentalBook(userId: String, bookId: String, bookTitle: String): RentalDto {
        val rental = rentalRepository.findByUserId(userId)
            .orElseThrow{ -> throw IllegalArgumentException("없는 ID입니다.")}

        rental.checkRentalAvailable()
        val rentBook = rental.rentBook(bookId, bookTitle)
        rentalRepository.save(rental)

        rentalProducer.sendForStockEvent(BookStockEventDto(rentBook.bookId, BookStock.UNAVAILABLE))
        rentalProducer.sendForStatusEvent(BookStatusEventDto(rentBook.bookId, BookStatus.RENT_BOOK))
        rentalProducer.sendForDeliveryEvent(RentalEventDto(rental.rentalId, rental.userId))

        return RentalDto(rental.id, rental.userId, rental.bookId)
    }

    @Transactional
    fun returnBook(userId: String, bookId: String): RentalDto {
        val rental = rentalRepository.findByUserId(userId)
            .orElseThrow{ -> throw IllegalArgumentException("없는 ID입니다.")}

        val overdueBook = rental.overdueBook(bookId)
        rentalRepository.save(rental)

        rentalProducer.sendForStockEvent(BookStockEventDto(overdueBook.bookId, BookStock.AVAILABLE))
        rentalProducer.sendForStatusEvent(BookStatusEventDto(overdueBook.bookId, BookStatus.RETURN_BOOK))

        return RentalDto(rental.id, rental.userId, rental.bookId)
    }

    @Transactional
    fun overdueBook(userId: String, bookId: String): RentalDto {
        val rental = rentalRepository.findByUserId(userId)
            .orElseThrow{ -> throw IllegalArgumentException("없는 ID입니다.")}
        val overdueBook = rental.overdueBook(bookId)
        rentalRepository.save(rental)

        rentalProducer.sendForStockEvent(BookStockEventDto(overdueBook.bookId, BookStock.OVERDUE_BOOK))

        return RentalDto(rental.id, rental.userId, rental.bookId)
    }

    @Transactional
    fun returnOverdueBook(userId: String, bookId: String): RentalDto {
        val rental = rentalRepository.findByUserId(userId)
            .orElseThrow{ -> throw IllegalArgumentException("없는 ID입니다.")}

        val returnedOverdueBook = rental.returnedOverdueBook(bookId)
        rentalRepository.save(rental)

        rentalProducer.sendForStockEvent(BookStockEventDto(returnedOverdueBook.bookId, BookStock.AVAILABLE))
        rentalProducer.sendForStatusEvent(BookStatusEventDto(returnedOverdueBook.bookId, BookStatus.RETURN_BOOK))

        return RentalDto(rental.id, rental.userId, rental.bookId)
    }
}