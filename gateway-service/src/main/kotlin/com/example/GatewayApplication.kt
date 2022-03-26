package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
class GatewayServiceApplication

fun main(args: Array<String>) {
	runApplication<GatewayServiceApplication>(*args)
}
