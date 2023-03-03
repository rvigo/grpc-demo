package com.rvigo.grpcPoc

import io.grpc.BindableService
import io.grpc.ServerBuilder
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GrpcPocApplication(val services: List<BindableService>) : ApplicationRunner {
    private val logger = KotlinLogging.logger {}

    override fun run(args: ApplicationArguments?) {
        val port = 9090
        val serverBuilder = ServerBuilder.forPort(port)
        services.forEach {
            serverBuilder.addService(it)
        }
        serverBuilder
            .build()
            .start()

        logger.info { "gPRC server started at :${port}" }
    }
}

fun main(args: Array<String>) {
    runApplication<GrpcPocApplication>(*args)
}
