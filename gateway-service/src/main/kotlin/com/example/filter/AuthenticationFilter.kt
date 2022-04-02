package com.example.filter

import io.jsonwebtoken.Jwts
import org.apache.http.HttpHeaders
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthenticationFilter(
    private val env:Environment
): AbstractGatewayFilterFactory<AuthenticationFilter.Config>(Config::class.java) {

    private val log = LoggerFactory.getLogger(LoggingGlobalFilter::class.java)

    override fun apply(config: Config?): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            val authenticationHeader = exchange.request.headers.get(HttpHeaders.AUTHORIZATION)?.get(0)
            authenticationHeader ?:
                return@GatewayFilter onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED)

            val jwtToken = authenticationHeader.replace("Bearer", "")
            if(!isValidatedToken(jwtToken)) {
                return@GatewayFilter onError(exchange, "JWT Token is not validated", HttpStatus.UNAUTHORIZED)
            }

            chain.filter(exchange)
        }
    }

    private fun isValidatedToken(jwtToken: String): Boolean {
        var returnValue = true;
        var subject: String? = null

        try {
            subject = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
                .parseClaimsJws(jwtToken).body.subject
        } catch(ex: Exception){
            returnValue = false;
        }

        if(!StringUtils.hasText(subject)) {
            returnValue = false;
        }
        return returnValue
    }

    private fun onError(exchange: ServerWebExchange, errorMessage: String, unauthorized: HttpStatus): Mono<Void> {
        log.error(errorMessage)
        exchange.response.statusCode = unauthorized
        return exchange.response.setComplete()
    }

    override fun getConfigClass(): Class<Config> {
        return Config::class.java
    }

    override fun newConfig(): Config {
        return Config()
    }

    data class Config(
        val simpleName: String
    ) {
        constructor(): this("")
    }
}