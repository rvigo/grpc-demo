package com.rvigo.grpcPoc.configurations

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChannelConfiguration(
    @Value("\${person.grpc.url}")
    val url: String,
    @Value("\${person.grpc.port}")
    val port: Int,
) {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun getChannel(): ManagedChannel {
        logger.info { "building channel for \"$url:$port\"" }
        val channel = ManagedChannelBuilder.forAddress(
            url,
            port
        ).usePlaintext().build()
        logger.info { "channel built" }

        return channel
    }
}
