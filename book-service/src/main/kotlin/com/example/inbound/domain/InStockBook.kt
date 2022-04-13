package com.example.inbound.domain

import com.example.outbound.dto.InStockBookDto
import org.apache.commons.lang.StringUtils
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
class InStockBook(
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
    var source:Source,

    var bookId: String
) {
    fun update(inStockBookDto: InStockBookDto) {
        this.title = if(StringUtils.isNotEmpty(inStockBookDto.title)) inStockBookDto.title!! else this.title
        this.description = if(StringUtils.isNotEmpty(inStockBookDto.description)) inStockBookDto.description!! else this.description
        this.author = if(StringUtils.isNotEmpty(inStockBookDto.author)) inStockBookDto.author!! else this.author
        this.publisher = if(StringUtils.isNotEmpty(inStockBookDto.publisher)) inStockBookDto.publisher!! else this.publisher
        this.isbn = if(inStockBookDto.isbn != null && inStockBookDto.isbn!!.compareTo(0L) > 0) ISBN(inStockBookDto.isbn!!) else this.isbn
        this.publicationDate = if(inStockBookDto.publicationDate != null) inStockBookDto.publicationDate!! else this.publicationDate
        this.source = if(inStockBookDto.source != null) inStockBookDto.source!! else this.source
    }

    companion object {
        fun createByDto(inStockBookDto: InStockBookDto): InStockBook {
            return InStockBook(
                0L, inStockBookDto.title, inStockBookDto.description, inStockBookDto.author,
                inStockBookDto.publisher, ISBN(inStockBookDto.isbn), inStockBookDto.publicationDate,
                inStockBookDto.source, UUID.randomUUID().toString()
            )
        }
    }

}