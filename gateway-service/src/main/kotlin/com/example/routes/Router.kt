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
            .route {r:PredicateSpec ->
                r.path("/rental-service/**")
                    .filters{f ->
                        f.filter(loggingGlobalFilter.apply(LoggingGlobalFilter.Config("RENTAL-SERVICE", true, true)))
                            .filter(authenticationFilter.apply(AuthenticationFilter.Config("")))
                            .rewritePath("/rental-service/(?<segment>/*", "/$\\{segment}")
                    }
                    .uri("lb://RENTAL-SERVICE")
            }
            .route { r:PredicateSpec ->
                r.path("/book-service/**")
                    .filters{f ->
                        f.filter(loggingGlobalFilter.apply(LoggingGlobalFilter.Config("BOOK-SERVICE", true, true)))
                            .filter(authenticationFilter.apply(AuthenticationFilter.Config("")))
                            .rewritePath("/book-service/(?<segment>/*", "/$\\{segment}")
                    }
                    .uri("lb://BOOK-SERVICE")
            }
            .route { r: PredicateSpec ->
                r.path("/delivery-service/**")
                    .filters{ f ->
                        f.filter(loggingGlobalFilter.apply(LoggingGlobalFilter.Config("DELIVERY-SERVICE", true, true)))
                            .filter(authenticationFilter.apply(AuthenticationFilter.Config("")))
                            .rewritePath("/delivery-service/(?<segment>/*", "/$\\{segment}")
                    }
                    .uri("lb://DELIVERY-SERVICE")
            }
            .build()
    }
}
