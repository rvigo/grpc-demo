package com.rvigo.grpcPoc.adapters.grpc

import com.rvigo.grpcPoc.adapters.grpc.person.Person.*
import com.rvigo.grpcPoc.adapters.grpc.person.PersonServiceGrpc
import io.grpc.stub.StreamObserver
import mu.KotlinLogging
import org.springframework.stereotype.Service
import com.rvigo.grpcPoc.repositories.PersonRepository

@Service
class PersonService(val repository: PersonRepository) : PersonServiceGrpc.PersonServiceImplBase() {
    private val logger = KotlinLogging.logger {}

    override fun findPerson(request: PersonRequest?, responseObserver: StreamObserver<PersonResponse>?) {
        logger.info { "got a request: $request" }
        val person = repository.findPersonByCpfOrNull(request!!.cpf)
            ?: throw RuntimeException("entity not found") // TODO how to properly handle errors????

        val personResponse = PersonResponse
            .newBuilder()
            .setName(person.name)
            .setEmail(person.email)
            .setCpf(person.cpf)
            .setId(person.id.value).build()
        logger.info { "sending a response: $person" }
        responseObserver?.onNext(personResponse)
        responseObserver?.onCompleted()
    }
}
