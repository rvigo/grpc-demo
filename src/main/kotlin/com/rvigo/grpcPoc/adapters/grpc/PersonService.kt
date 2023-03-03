package com.rvigo.grpcPoc.adapters.grpc

import com.rvigo.grpcPoc.adapters.grpc.person.Person.*
import com.rvigo.grpcPoc.adapters.grpc.person.PersonServiceGrpc
import com.rvigo.grpcPoc.exceptions.PersonNotFoundException
import io.grpc.stub.StreamObserver
import mu.KotlinLogging
import org.springframework.stereotype.Service
import com.rvigo.grpcPoc.repositories.PersonRepository
import io.grpc.Status.INTERNAL
import io.grpc.Status.NOT_FOUND

@Service
class PersonService(val repository: PersonRepository) : PersonServiceGrpc.PersonServiceImplBase() {
    private val logger = KotlinLogging.logger {}

    override fun findPerson(request: PersonRequest, responseObserver: StreamObserver<PersonResponse>?) {
        runCatching {
            logger.info { "got a request: $request" }

            val person = repository.findPersonByCpfOrNull(request.cpf)
                ?: throw PersonNotFoundException(request.cpf)

            val personResponse = PersonResponse.newBuilder()
                .setName(person.name)
                .setEmail(person.email)
                .setCpf(person.cpf)
                .setId(person.id).build()
            logger.info { "sending a response: $person" }
            responseObserver?.onNext(personResponse)
            responseObserver?.onCompleted()
        }.onFailure {
            val status = when (it) {
                is PersonNotFoundException -> NOT_FOUND
                else -> INTERNAL
            }

            val errorResponse = ErrorResponse.newBuilder()
                .setMessage(it.message)
                .setException(it::class.java.name)
                .build()

            responseObserver?.onError(
                status.withDescription(errorResponse.message).asRuntimeException()
            )
        }
    }
}
