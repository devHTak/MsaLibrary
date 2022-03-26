package com.example.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class LoggingGlobalFilter: AbstractGatewayFilterFactory<LoggingGlobalFilter.Config>(Config::class.java) {

    private val log = LoggerFactory.getLogger(LoggingGlobalFilter::class.java)

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange: ServerWebExchange, chain: GatewayFilterChain ->
            if(config.preLogger) {
                log.info("Global Pre Filter: request id -> {}", exchange.request.id)
            }
            chain.filter(exchange).then(Mono.fromRunnable {
                if(config.postLogger) {
                    log.info("Global Post Filter: response status -> {}", exchange.response.statusCode)
                }
            })
        }

    }

    override fun getConfigClass(): Class<Config> {
        return Config::class.java
    }

    override fun newConfig(): Config {
        return Config()
    }

    data class Config(
        val baseMessage: String,
        val preLogger: Boolean,
        val postLogger: Boolean
    ) {
        constructor(): this("", false, false)
    }
}