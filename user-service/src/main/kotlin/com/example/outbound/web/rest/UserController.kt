package com.example.outbound.web.rest

import com.example.inbound.service.UserService
import com.example.outbound.dto.PointDto
import com.example.outbound.dto.SaveUserDto
import com.example.outbound.dto.UserDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class UserController(
    private val userService: UserService
) {

    @PostMapping("/users")
    fun saveUser(@RequestBody saveUserDto:SaveUserDto): ResponseEntity<UserDto> {
        val userDto = userService.saveUser(saveUserDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto)
    }

    @GetMapping("/users/{userId}")
    fun retrieveUser(@PathVariable userId:String):ResponseEntity<UserDto> {
        val userDto = userService.findByUserId(userId)
        return ResponseEntity.ok(userDto)
    }

    @PutMapping("/users/{userId}")
    fun updateUser(@PathVariable userId: String, @RequestBody saveUserDto: SaveUserDto):ResponseEntity<UserDto> {
        val userDto = userService.updateByUserId(userId, saveUserDto)
        return ResponseEntity.ok(userDto)
    }

    @DeleteMapping("/users/{userId}")
    fun deleteUser(@PathVariable userId:String): ResponseEntity<UserDto> {
        val userDto = userService.deleteUser(userId)
        return ResponseEntity.ok(userDto)
    }

    @PostMapping("/users/{userId}/calculatePoint")
    fun calculatePointByUserId(@PathVariable userId: String, @RequestBody point: PointDto): ResponseEntity<UserDto> {
        val userDto = userService.calculatePoint(userId, point)
        return ResponseEntity.ok(userDto)
    }

    @GetMapping("/health_check")
    fun healthCheck(): String {
        return "Hello UserService"
    }
}