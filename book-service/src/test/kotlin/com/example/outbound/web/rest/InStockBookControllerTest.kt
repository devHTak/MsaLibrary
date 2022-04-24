package com.example.outbound.web.rest

import com.example.inbound.domain.Source
import com.example.inbound.service.BookService
import com.example.inbound.service.InStockBookService
import com.example.outbound.dto.InStockBookDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
class InStockBookControllerTest {
    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var inStockBookService: InStockBookService
    @Autowired private lateinit var objectMapper: ObjectMapper

    @Test
    @DisplayName("재고 책 생성 성공")
    fun saveInStockBookSuccessTest() {
        val inStockBookDto = InStockBookDto("TEST", "", "", "", 2323L, LocalDateTime.now(), Source.Donated)

        mockMvc.perform(post("/in-stock")
                    .content(objectMapper.writeValueAsString(inStockBookDto))
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.bookId").exists())
            .andExpect(jsonPath("$.title").value("TEST"))
    }

    @Test
    @DisplayName("재고 책 정보 조회")
    fun findInStockBookInfoSuccessTest() {
        val inStockBookDto = InStockBookDto("TEST", "", "", "", 2323L, LocalDateTime.now(), Source.Donated)
        val bookDto = inStockBookService.save(inStockBookDto)

        mockMvc.perform(get("/in-stock/{bookId}", bookDto.bookId)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.bookId").value(bookDto.bookId))
            .andExpect(jsonPath("$.title").value(bookDto.title))
    }

    @Test
    @DisplayName("재고 책 정보 조회 실패")
    fun findInStockBookInfoFailTest() {
        mockMvc.perform(get("/in-stock/{bookId}", "TEST")
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect{rslt ->
                assertTrue(rslt.resolvedException!!.javaClass.isAssignableFrom(IllegalArgumentException::class.java))
            }
    }

    @Test
    @DisplayName("재고 책 정보 업데이트")
    fun updateInStockBookInfoSuccessTest() {
        val inStockBookDto = InStockBookDto("TEST", "", "", "", 2323L, LocalDateTime.now(), Source.Donated)
        val bookDto = inStockBookService.save(inStockBookDto)
        val updateInStockBookDto = InStockBookDto("UPDATE_TEST", "", "", "", 2323L, LocalDateTime.now(), Source.Donated)

        mockMvc.perform(put("/in-stock/{bookId}", bookDto.bookId)
                    .content(objectMapper.writeValueAsString(updateInStockBookDto))
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.bookId").value(bookDto.bookId))
            .andExpect(jsonPath("$.title").value(updateInStockBookDto.title))
    }

    @Test
    @DisplayName("재고 책 정보 업데이트 실패")
    fun updateInStockBookInfoFailTest() {
        val updateInStockBookDto = InStockBookDto("UPDATE_TEST", "", "", "", 2323L, LocalDateTime.now(), Source.Donated)
        mockMvc.perform(put("/in-stock/{bookId}", "TEST")
                    .content(objectMapper.writeValueAsString(updateInStockBookDto))
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect{rslt ->
                assertTrue(rslt.resolvedException!!.javaClass.isAssignableFrom(IllegalArgumentException::class.java))
            }
    }

    @Test
    @DisplayName("재고 책 정보 삭제")
    fun deleteInStockBookInfoSuccessTest() {
        val inStockBookDto = InStockBookDto("TEST", "", "", "", 2323L, LocalDateTime.now(), Source.Donated)
        val bookDto = inStockBookService.save(inStockBookDto)
        val updateInStockBookDto = InStockBookDto("UPDATE_TEST", "", "", "", 2323L, LocalDateTime.now(), Source.Donated)

        mockMvc.perform(
            delete("/in-stock/{bookId}", bookDto.bookId)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk)
    }

    @Test
    @DisplayName("재고 책 정보 삭제 실패")
    fun deleteInStockBookInfoFailTest() {
        mockMvc.perform(delete("/in-stock/{bookId}", "TEST")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect{rslt ->
                assertTrue(rslt.resolvedException!!.javaClass.isAssignableFrom(IllegalArgumentException::class.java))
            }
    }
}