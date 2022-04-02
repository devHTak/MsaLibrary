package com.example.inbound.config

import com.example.inbound.service.UserService
import com.example.outbound.dto.LoginDto
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.core.env.Environment
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.io.IOException
import java.lang.RuntimeException
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.ArrayList

class AuthenticationFilter(
    private val userService: UserService,
    private val env: Environment
): UsernamePasswordAuthenticationFilter() {

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        authResult ?: throw RuntimeException()
        response ?: throw RuntimeException()

        val user: User = authResult.principal as User
        val email = user.username
        val userDto = userService.getUserDetailsByEmail(email)

        val token = Jwts.builder()
            .setSubject(userDto.userId)
            .setExpiration(
                Date(System.currentTimeMillis() + env.getProperty("token.expiration_time")!!.toLong())
            )
            .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
            .compact()

        response.addHeader("token", token)
        response.addHeader("userId", userDto.userId)
    }

    override fun attemptAuthentication(
        request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        request ?: throw RuntimeException()

        val credit = ObjectMapper().readValue(request.inputStream, LoginDto::class.java)
        return authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(credit.email, credit.password, ArrayList())
        )
    }
}