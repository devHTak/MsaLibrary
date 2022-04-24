package com.example.outbound.web.rest


import com.example.inbound.service.BookService
import com.example.inbound.service.InStockBookService
import com.example.outbound.dto.BookDto
import com.example.outbound.dto.InStockBookDto
import com.example.outbound.dto.UpdateBookDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class BookControllerTest {
    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var bookService: BookService
    @Autowired lateinit var inStockBookService: InStockBookService
    @Autowired lateinit var objectMapper: ObjectMapper

    private lateinit var inStockBookDto: InStockBookDto
    private lateinit var inStockBook: BookDto

    @BeforeEach
    fun beforeEach() {
        inStockBookDto = InStockBookDto("TEST", "", "", "", 2323L, LocalDateTime.now(), null)
        inStockBook = inStockBookService.save(inStockBookDto)
    }

    @Test
    @DisplayName("북 저장 성공")
    fun saveBookSuccessTest() {
        mockMvc.perform(post("/books/{bookId}", inStockBook.bookId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.bookId").value(inStockBook.bookId))
            .andExpect(jsonPath("$.title").value(inStockBook.title))
    }

    @Test
    @DisplayName("북 저장 실패")
    fun saveBookFailTest() {
        mockMvc.perform(post("/books/{bookId}", "TEST")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect{rslt ->
                assertTrue(rslt.resolvedException!!.javaClass.isAssignableFrom(IllegalArgumentException::class.java))
            }
    }

    @Test
    @DisplayName("북 정보 조회 성공")
    fun findBookInfoSuccessTest() {
        val bookDto = bookService.saveBook(inStockBook.bookId)

        mockMvc.perform(get("/books/{bookId}", bookDto.bookId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.bookId").value(bookDto.bookId))
            .andExpect(jsonPath("$.title").value(bookDto.title))
    }

    @Test
    @DisplayName("북 정보 조회 실패")
    fun findBookInfoFailTest() {
        mockMvc.perform(get("/books/{bookId}", "TEST")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect{rslt ->
                assertTrue(rslt.resolvedException!!.javaClass.isAssignableFrom(IllegalArgumentException::class.java))
            }
    }

    @Test
    @DisplayName("북 업데이트 성공")
    fun updateBookInfoSuccessTest() {
        val bookDto = bookService.saveBook(inStockBook.bookId)
        val updateBookDto = UpdateBookDto("TEST2", "", "", "", 55L, LocalDateTime.now(), null, null, null)

        mockMvc.perform(put("/books/{bookId}", bookDto.bookId)
                        .content(objectMapper.writeValueAsString(updateBookDto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.bookId").value(bookDto.bookId))
            .andExpect(jsonPath("$.title").value(updateBookDto.title))
    }

    @Test
    @DisplayName("북 업데이트 실패")
    fun updateBookInfoFailTest() {
        val updateBookDto = UpdateBookDto("TEST2", "", "", "", 55L, LocalDateTime.now(), null, null, null)

        mockMvc.perform(put("/books/{bookId}", "TEST")
                        .content(objectMapper.writeValueAsString(updateBookDto))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect{rslt ->
                assertTrue(rslt.resolvedException!!.javaClass.isAssignableFrom(IllegalArgumentException::class.java))
            }
    }

    @Test
    @DisplayName("북 삭제 성공")
    fun deleteBookSuccessTest() {
        val bookDto = bookService.saveBook(inStockBook.bookId)
        mockMvc.perform(delete("/books/{bookId}", bookDto.bookId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("북 삭제 실패")
    fun deleteBookFailTest() {
        mockMvc.perform(delete("/books/{bookId}", "TEST")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect{rslt ->
                assertTrue(rslt.resolvedException!!.javaClass.isAssignableFrom(java.lang.IllegalArgumentException::class.java))
            }
    }
}