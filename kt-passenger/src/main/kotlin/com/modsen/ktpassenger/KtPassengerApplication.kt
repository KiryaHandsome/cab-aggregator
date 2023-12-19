package com.modsen.ktpassenger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class KtPassengerApplication

fun main(args: Array<String>) {
    runApplication<KtPassengerApplication>(*args)
}
