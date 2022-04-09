package com.example.outbound.web.rest

import com.example.inbound.domain.Rental
import com.example.inbound.repository.RentalRepository
import com.example.inbound.service.RentalService
import com.example.outbound.adaptor.client.BookClient
import com.example.outbound.dto.BookDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.transaction.annotation.Transactional

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
internal class RentalControllerTest{


     @Autowired lateinit var mockMvc: MockMvc
     @Autowired lateinit var rentalRepository: RentalRepository
     @Autowired lateinit var rentalService: RentalService
     @MockBean lateinit var bookClient: BookClient

     @Test
     @DisplayName("도서 대여 성공")
     fun rentalBookSuccessTest() {
          // given
          val bookId = "BOOK-TEST-SUCCESS"
          val userId = "USER-TEST-SUCCESS"
          `when`(bookClient.findBookInfo(bookId)).thenReturn(ResponseEntity.ok(BookDto(bookId, "TEST")))

          val rental = Rental()
          rental.userId = userId
          rental.bookId = bookId
          rentalRepository.save(rental);

          // when then
          mockMvc.perform(post("/rentals/{userId}/rentedItem/{bookId}", userId, bookId)
                         .contentType(MediaType.APPLICATION_JSON_VALUE)
                         .accept(MediaType.APPLICATION_JSON_VALUE))
               .andDo(print())
               .andExpect(status().isCreated)
               .andExpect(jsonPath("$.userId").value(userId))
               .andExpect(jsonPath("$.bookId").value(bookId))
               .andExpect(jsonPath("$.id").exists())
     }

     @Test
     @DisplayName("도서 대여 실패 - BookClient error")
     fun rentalBookFailByBookClientTest() {
          // given
          val bookId = "BOOK-TEST-FAIL"
          val userId = "USER-TEST-FAIL"
          `when`(bookClient.findBookInfo(bookId)).thenThrow(IllegalArgumentException::class.java)

          //when then
          mockMvc.perform(post("/rentals/{userId}/rentedItem/{bookId}", userId, bookId)
                         .accept(MediaType.APPLICATION_JSON_VALUE)
                         .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andDo(print())
               .andExpect(status().isBadRequest)
               .andExpect{rslt ->
                    assertTrue(rslt.resolvedException!!.javaClass.isAssignableFrom(IllegalArgumentException::class.java))
               }
     }

     @Test
     @DisplayName("도서 대여 실패 - BookRepository 없는 ID")
     fun rentalBookRepositoryFailTest() {
          // given
          val bookId = "BOOK-ID-FAIL"
          val userId = "USER-ID-FAIL"
          `when`(bookClient.findBookInfo(bookId)).thenReturn(ResponseEntity.ok(BookDto(bookId, "TEST")))

          // when, then
          mockMvc.perform(post("/rentals/{userId}/rentedItem/{bookId}", userId, bookId)
                         .accept(MediaType.APPLICATION_JSON_VALUE)
                         .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andDo(print())
               .andExpect(status().is4xxClientError)
               .andExpect{result -> assertTrue(
                    result.resolvedException!!.javaClass.isAssignableFrom(IllegalArgumentException::class.java)
               )}
     }

     @Test
     @DisplayName("대여 책 반납 성공")
     fun returnBookSuccessTest() {
          // given
          val userId = "USER-ID-SUCCESS"
          val bookId = "BOOK-ID-SUCCESS"
          var rental = Rental()
          rental.userId = userId
          rental.bookId = bookId
          rentalRepository.save(rental)
          rentalService.rentalBook(userId, bookId, "TEST")

          // when then
          mockMvc.perform(delete("/rentals/{userId}/rentedItem/{bookId}", userId, bookId)
                         .accept(MediaType.APPLICATION_JSON_VALUE)
                         .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andDo(print())
               .andExpect(status().isOk)
               .andExpect(jsonPath("$.userId").value(userId))
               .andExpect(jsonPath("$.bookId").value(bookId))
               .andExpect(jsonPath("$.id").exists())
     }

     @Test
     @DisplayName("책 연체 성공")
     fun overdueBookSuccessTest() {
          // given
          val userId = "USER-ID-SUCCESS"
          val bookId = "BOOK-ID-SUCCESS"
          var rental = Rental()
          rental.userId = userId
          rental.bookId = bookId
          rentalRepository.save(rental)
          rentalService.rentalBook(userId, bookId, "TEST")

          // when then
          mockMvc.perform(post("/rentals/{userId}/overdueItem/{bookId}", userId, bookId)
               .accept(MediaType.APPLICATION_JSON_VALUE)
               .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andDo(print())
               .andExpect(status().isCreated)
               .andExpect(jsonPath("$.userId").value(userId))
               .andExpect(jsonPath("$.bookId").value(bookId))
               .andExpect(jsonPath("$.id").exists())
     }

     @Test
     @DisplayName("책 연체 반납 성공")
     fun returnOverdueBookSuccessTest() {
          // given
          val userId = "USER-ID-SUCCESS"
          val bookId = "BOOK-ID-SUCCESS"

          var rental = Rental()
          rental.userId = userId
          rental.bookId = bookId
          rentalRepository.save(rental)
          rentalService.rentalBook(userId, bookId, "TEST")
          rentalService.overdueBook(userId, bookId)

          // when then
          mockMvc.perform(delete("/rentals/{userId}/overdueItem/{bookId}", userId, bookId)
               .accept(MediaType.APPLICATION_JSON_VALUE)
               .contentType(MediaType.APPLICATION_JSON_VALUE))
               .andDo(print())
               .andExpect(status().isOk)
               .andExpect(jsonPath("$.userId").value(userId))
               .andExpect(jsonPath("$.bookId").value(bookId))
               .andExpect(jsonPath("$.id").exists())
     }

}