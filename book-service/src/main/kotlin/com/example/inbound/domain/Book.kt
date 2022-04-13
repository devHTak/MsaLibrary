package com.example.inbound.domain

import com.example.outbound.dto.UpdateBookDto
import org.apache.commons.lang.StringUtils
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Book(
    @Id @GeneratedValue
    var id: Long,

    var title: String,
    var description: String,
    var author: String,
    var publisher: String,

    @Embedded
    var isbn: ISBN,

    var publicationDate: LocalDateTime,

    @Enumerated(EnumType.STRING)
    var classificattion: Classificattion?,

    @Enumerated(EnumType.STRING)
    var bookStatus: BookStatus?,

    @Enumerated(EnumType.STRING)
    var location: Location?,

    var bookId: String
) {
    fun update(updateBookDto: UpdateBookDto) {
        this.title = if(StringUtils.isNotEmpty(updateBookDto.title)) updateBookDto.title!! else this.title
        this.description = if(StringUtils.isNotEmpty(updateBookDto.description)) updateBookDto.description!! else this.description
        this.author = if(StringUtils.isNotEmpty(updateBookDto.author)) updateBookDto.author!! else this.author
        this.publisher = if(StringUtils.isNotEmpty(updateBookDto.publisher)) updateBookDto.publisher!! else this.publisher
        this.isbn = if(updateBookDto.isbn != null && updateBookDto.isbn!!.compareTo(0L) > 0) ISBN(updateBookDto.isbn!!) else this.isbn
        this.publicationDate = if(updateBookDto.publicationDate != null) updateBookDto.publicationDate!! else this.publicationDate
        this.classificattion= if(updateBookDto.classificattion != null) updateBookDto.classificattion!! else this.classificattion
        this.bookStatus = if(updateBookDto.bookStatus != null) updateBookDto.bookStatus!! else this.bookStatus
        this.location = if(updateBookDto.location != null) updateBookDto.location!! else this.location
    }

    companion object {
        fun createByInStockBook(inStockBook: InStockBook) : Book {
            return Book(
                0L, inStockBook.title, inStockBook.description, inStockBook.author,
                inStockBook.publisher, inStockBook.isbn, inStockBook.publicationDate, null,
                null, null, inStockBook.bookId
            )
        }
    }
}