package com.example.inbound.service

import com.example.inbound.domain.User
import com.example.inbound.repository.UserRepository
import com.example.outbound.dto.PointDto
import com.example.outbound.dto.SaveUserDto
import com.example.outbound.dto.UserDto
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional
import kotlin.IllegalArgumentException

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
): UserDetailsService {
    fun saveUser(saveUserDto: SaveUserDto): UserDto {
        val user = User()
        saveUserDto.password = passwordEncoder.encode(saveUserDto.password)
        user.convertBySaveUserDto(saveUserDto)

        val saveUser = userRepository.save(user)
        return this.convertUserDtoByUser(saveUser)
    }

    fun findByUserId(userId: String): UserDto? {
        val user = userRepository.findByUserId(userId)
            .orElseThrow { IllegalArgumentException("없는 ID입니다.") }

        return this.convertUserDtoByUser(user)
    }

    fun deleteUser(userId: String): UserDto {
        val user = userRepository.findByUserId(userId)
            .orElseThrow { IllegalArgumentException("없는 ID입니다.") }

        user.isUse = false
        return this.convertUserDtoByUser(user)
    }

    fun updateByUserId(userId: String, saveUserDto: SaveUserDto): UserDto {
        val user = userRepository.findByUserId(userId)
            .orElseThrow { IllegalArgumentException("없는 ID입니다.") }

        user.convertBySaveUserDto(saveUserDto)
        return this.convertUserDtoByUser(user)
    }

    fun calculatePoint(userId: String, point: PointDto): UserDto {
        val user = userRepository.findByUserId(userId)
            .orElseThrow { IllegalArgumentException("없는 ID입니다.") }

        user.calcualtePoint(point)
        return this.convertUserDtoByUser(user)
    }

    private fun convertUserDtoByUser(user:User):UserDto {
        val userDto = UserDto()
        userDto.convertByUser(user)
        return userDto
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        username ?:throw UsernameNotFoundException(username)

        val user = userRepository.findByEmail(username)
            .orElseThrow{ UsernameNotFoundException(username) }

        return org.springframework.security.core.userdetails.User(user.email, user.password, ArrayList())
    }

    fun getUserDetailsByEmail(email: String): UserDto {
        var user = userRepository.findByEmail(email)
            .orElseThrow{ IllegalArgumentException("없는 ID입니다.") }
        return this.convertUserDtoByUser(user)
    }
}