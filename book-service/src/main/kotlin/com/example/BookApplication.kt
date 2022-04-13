package com.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication
@EnableEurekaClient
class BookServiceApplication

fun main(args: Array<String>) {
	runApplication<BookServiceApplication>(*args)
}