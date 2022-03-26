package com.example.routes

import com.example.filter.LoggingGlobalFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class Router {

    @Bean
    fun route(builder: RouteLocatorBuilder, loggingGlobalFilter: LoggingGlobalFilter): RouteLocator? {
        return builder.routes()
            .route { r: PredicateSpec ->
                r.path("/**")
                    .filters { f ->
                        f.filter(loggingGlobalFilter.apply(LoggingGlobalFilter.Config("Base Message", true, true)))
                    }
                    .uri("")
            }.build()
    }
}
