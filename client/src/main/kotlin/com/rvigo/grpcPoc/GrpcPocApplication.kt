package com.rvigo.grpcPoc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcPocApplication

fun main(args: Array<String>) {
    runApplication<GrpcPocApplication>(*args)
}
