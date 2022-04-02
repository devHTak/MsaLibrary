package com.example.routes

import com.example.filter.AuthenticationFilter
import com.example.filter.LoggingGlobalFilter
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod

@Configuration
class Router {

    @Bean
    fun route(builder: RouteLocatorBuilder, loggingGlobalFilter: LoggingGlobalFilter, authenticationFilter: AuthenticationFilter): RouteLocator? {
        return builder.routes()
            .route {
                r:PredicateSpec ->
                    r.path("/user-service/login", "/user-service/actuator", "/user-service/health_check")
                        .filters {f ->
                            f.filter(loggingGlobalFilter.apply(LoggingGlobalFilter.Config("USER-SERVICE", true, true)))
                                .rewritePath("/user-service/(?<segment>/*)", "/$\\{segment}" )
                        }
                        .uri("lb://USER-SERVICE")
            }
            .route {
                r:PredicateSpec ->
                    r.path("/user-service/users")
                        .and().method(HttpMethod.POST)
                        .filters {f ->
                            f.filter(loggingGlobalFilter.apply(LoggingGlobalFilter.Config("USER-SERVICE", true, true)))
                                .rewritePath("/user-service/(?<segment>/*)", "/$\\{segment}" )
                        }
                        .uri("lb://USER-SERVICE")
            }
            .route { r:PredicateSpec ->
                r.path("/user-service/**")
                    .filters {f ->
                        f.filter(loggingGlobalFilter.apply(LoggingGlobalFilter.Config("USER-SERVICE", true, true)))
                            .filter(authenticationFilter.apply(AuthenticationFilter.Config("")))
                            .rewritePath("/user-service/(?<segment>/*)", "/$\\{segment}" )
                    }
                    .uri("lb://USER-SERVICE")
            }
            .build()
    }
}
