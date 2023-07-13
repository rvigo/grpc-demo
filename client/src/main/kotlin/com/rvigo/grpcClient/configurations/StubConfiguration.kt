package com.rvigo.grpcClient.configurations

import com.rvigo.grpcServer.adapters.grpc.person.PersonServiceGrpc
import io.grpc.ManagedChannel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StubConfiguration {

    @Bean
    fun stub(managedChannel: ManagedChannel): PersonServiceGrpc.PersonServiceBlockingStub =
        PersonServiceGrpc.newBlockingStub(managedChannel)
}
