package com.example.outbound.web.rest

import com.example.inbound.domain.Delivery
import com.example.inbound.repository.DeliveryRepository
import com.example.inbound.service.DeliveryService
import com.example.outbound.dto.DeliveryDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matcher
import org.junit.jupiter.api.Assertions.*
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
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException
import java.util.*
import org.hamcrest.Matchers.*

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class DeliveryControllerTest {

    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var deliveryService: DeliveryService
    @Autowired lateinit var deliveryRepository: DeliveryRepository
    @Autowired lateinit var objectMapper: ObjectMapper

    @Test
    fun saveDeliveryTest() {
        // given
        val deliveryDto = DeliveryDto("", "TEST-USER", "TEST-RENTAL")

        // when then
        mockMvc.perform(post("/deliveries")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(deliveryDto)))
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.userId").value("TEST-USER"))
            .andExpect(jsonPath("$.rentalId").value("TEST-RENTAL"))
    }

    @Test
    fun startDeliverySuccess() {
        // given
        val delivery = Delivery("TEST-DELIVERY", "TEST-USER", "TEST-RENTAL")
        val returnDelivery = deliveryRepository.save(delivery)
        val deliveryDto = DeliveryDto(returnDelivery.deliveryId, returnDelivery.userId, returnDelivery.rentalId)

        // when, then
        mockMvc.perform(put("/deliveries/status/start")
                .content(objectMapper.writeValueAsString(deliveryDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.userId").value("TEST-USER"))
            .andExpect(jsonPath("$.rentalId").value("TEST-RENTAL"))
    }

    @Test
    fun startDeliveryFail() {
        // given
        val completeDelivery = Delivery("TEST-DELIVERY", "TEST-USER", "TEST-RENTAL")
        completeDelivery.changeStatusToStart()
        completeDelivery.changeStatusToComplete()
        val returnDelivery = deliveryRepository.save(completeDelivery)
        val deliveryDto = DeliveryDto(returnDelivery.deliveryId, returnDelivery.userId, returnDelivery.rentalId)

        // when then
        mockMvc.perform(put("/deliveries/status/start")
                .content(objectMapper.writeValueAsString(deliveryDto))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect { r ->
                assertTrue(r.resolvedException!!.javaClass.isAssignableFrom(IllegalArgumentException::class.java))
            }
    }

    @Test
    fun completeDeliverySuccess() {
        // given
        val startDelivery = Delivery("TEST-DELIVERY", "TEST-USER", "TEST-RENTAL")
        startDelivery.changeStatusToStart()
        val returnDelivery = deliveryRepository.save(startDelivery)
        val deliveryDto = DeliveryDto(returnDelivery.deliveryId, returnDelivery.userId, returnDelivery.rentalId)

        // when, then
        mockMvc.perform(put("/deliveries/status/complete")
            .content(objectMapper.writeValueAsString(deliveryDto))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.userId").value("TEST-USER"))
            .andExpect(jsonPath("$.rentalId").value("TEST-RENTAL"))
    }

    @Test
    fun completeDeliveryFail() {
        // given
        val readyDelivery = Delivery("TEST-DELIVERY", "TEST-USER", "TEST-RENTAL")
        val returnDelivery = deliveryRepository.save(readyDelivery)
        val deliveryDto = DeliveryDto(returnDelivery.deliveryId, returnDelivery.userId, returnDelivery.rentalId)

        // when, then
        mockMvc.perform(put("/deliveries/status/complete")
            .content(objectMapper.writeValueAsString(deliveryDto))
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().is4xxClientError)
            .andExpect { r ->
                assertTrue(r.resolvedException!!.javaClass.isAssignableFrom(IllegalArgumentException::class.java))
            }
    }

    @Test
    fun retrieveBookStatusByUserIdSuccess() {
        // given
        arrayOf(
            Delivery(UUID.randomUUID().toString(), "TEST-USER", "TEST-RENTAL0"),
            Delivery(UUID.randomUUID().toString(), "TEST-USER", "TEST-RENTAL1"),
            Delivery(UUID.randomUUID().toString(), "TEST-USER", "TEST-RENTAL2"),
            Delivery(UUID.randomUUID().toString(), "TEST-USER", "TEST-RENTAL3"),
            Delivery(UUID.randomUUID().toString(), "TEST-USER", "TEST-RENTAL4")
        ).forEach(deliveryRepository::save)

        //when then
        mockMvc.perform(get("/deliveries/users/{userId}", "TEST-USER")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].userId").value("TEST-USER"))
            .andExpect(jsonPath("$.length()", `is`(5)))
    }

    fun retrieveBookStatusByRentalIdSuccess() {
        // given
        arrayOf(
            Delivery(UUID.randomUUID().toString(), "TEST-USER0", "TEST-RENTAL"),
            Delivery(UUID.randomUUID().toString(), "TEST-USER1", "TEST-RENTAL"),
            Delivery(UUID.randomUUID().toString(), "TEST-USER2", "TEST-RENTAL"),
            Delivery(UUID.randomUUID().toString(), "TEST-USER3", "TEST-RENTAL"),
            Delivery(UUID.randomUUID().toString(), "TEST-USER4", "TEST-RENTAL")
        ).forEach(deliveryRepository::save)

        //when then
        mockMvc.perform(get("/deliveries/rentals/{rentalId}", "TEST-RENTAL")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].rentalId").value("TEST-RENTAL"))
            .andExpect(jsonPath("$.length()", `is`(5)))
    }

}