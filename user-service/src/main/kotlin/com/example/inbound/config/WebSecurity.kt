package com.example.inbound.config

import com.example.inbound.service.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@EnableWebSecurity
class WebSecurity(
    private val userService: UserService,
    private val env:Environment,
    private val passwordEncoder: BCryptPasswordEncoder
): WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity?) {
        http!!.csrf().disable()
        http.authorizeRequests()
            .antMatchers("/actuator/**", "/health_check").permitAll()

        http.authorizeRequests()
                .antMatchers("/**").permitAll()
            .and()
            .addFilter(getAuthenticationFilter())

        http.headers().frameOptions().disable()
    }

    private fun getAuthenticationFilter(): AuthenticationFilter {
        var authenticationFilter = AuthenticationFilter(userService, env)
        authenticationFilter.setAuthenticationManager(authenticationManager())

        return authenticationFilter
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        // 인증 관련 설정
        auth!!.userDetailsService(userService).passwordEncoder(passwordEncoder)
    }
}