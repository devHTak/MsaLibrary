package com.example.outbound.web.rest

import com.example.inbound.repository.UserRepository
import com.example.inbound.service.UserService
import com.example.outbound.dto.CalculateType
import com.example.outbound.dto.PointDto
import com.example.outbound.dto.SaveUserDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.lang.IllegalArgumentException

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc
internal class UserControllerTest(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val objectMapper: ObjectMapper,
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserRepository
) {

    @Test
    @DisplayName("User 생성 - 성공")
    fun saveUserSuccessTest() {
        val saveUser = SaveUserDto("test", "test@test.com", "test1234", "", "", "")

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(saveUser)))
            .andDo(print())
            .andExpect(status().isCreated)
            .andExpect(jsonPath("username", "test").exists())
            .andExpect(jsonPath("userId").isString)
            .andExpect(jsonPath("email", "test@test.com").exists())
    }

    @Test
    @DisplayName("User 조회 - 성공 (미리 데이터 추가)")
    fun getUserByIdSucceessTest() {
        val saveUser = SaveUserDto("test", "test@test.com", "test1234", "", "", "")
        val userDto = userService.saveUser(saveUser)

        mockMvc.perform(get("/users/{userId}", userDto.userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("username", userDto.username).exists())
            .andExpect(jsonPath("email", userDto.email).exists())
            .andExpect(jsonPath("userId", userDto.userId).exists())
    }

    @Test
    @DisplayName("User 조회 - 존재하지 않는 ID, 실패")
    fun getUserByIdNotExists() {
        assumeTrue(userRepository.findByUserId("1").isEmpty)

        mockMvc.perform(get("/users/{userId}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE))
            .andDo(print())
            .andExpect(status().is4xxClientError)
//            .andExpect{result -> assertTrue(
//                result.resolvedException!!::class.java.isAssignableFrom(IllegalArgumentException::class.java)
//            )}
    }

    @Test
    @DisplayName("포인트 추가")
    fun savePointTest() {
        val saveUser = SaveUserDto("test", "test@test.com", "test1234", "", "", "")
        val userDto = userService.saveUser(saveUser)

        val point = PointDto(10, CalculateType.SAVE)

        mockMvc.perform(post("/users/{userId}/calculatePoint", userDto.userId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(point)))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("point", 10).exists())
    }

    @Test
    @DisplayName("포인트 삭제 - 성공")
    fun removePointSuccessTest() {
        val saveUser = SaveUserDto("test", "test@test.com", "test1234", "", "", "")
        val userDto = userService.saveUser(saveUser)

        val firstPoint = PointDto(20, CalculateType.SAVE)
        userService.calculatePoint(userDto.userId, firstPoint)

        val removePoint = PointDto(10, CalculateType.REMOVE)

        mockMvc.perform(post("/users/{userId}/calculatePoint", userDto.userId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(removePoint)))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("point", 10).exists())
    }

    @Test
    @DisplayName("포인트 삭제 - 실패")
    fun removePointFailTest() {
        val saveUser = SaveUserDto("test", "test@test.com", "test1234", "", "", "")
        val userDto = userService.saveUser(saveUser)

        val removePoint = PointDto(10, CalculateType.REMOVE)

        mockMvc.perform(post("/users/{userId}/calculatePoint", userDto.userId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(removePoint)))
            .andDo(print())
            .andExpect(status().is4xxClientError)
    }
}