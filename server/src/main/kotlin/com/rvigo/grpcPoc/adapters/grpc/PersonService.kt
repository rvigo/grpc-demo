package com.rvigo.grpcPoc.adapters.grpc

import com.rvigo.grpcPoc.adapters.grpc.person.Person.*
import com.rvigo.grpcPoc.adapters.grpc.person.PersonServiceGrpc
import com.rvigo.grpcPoc.exceptions.InvalidRequestException
import com.rvigo.grpcPoc.exceptions.PersonAlreadyExistsException
import com.rvigo.grpcPoc.exceptions.PersonCreationException
import com.rvigo.grpcPoc.exceptions.PersonNotFoundException
import com.rvigo.grpcPoc.infra.repositories.PersonRepository
import com.rvigo.grpcPoc.models.Address
import com.rvigo.grpcPoc.models.Person
import io.grpc.Status.*
import io.grpc.stub.StreamObserver
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class PersonService(val repository: PersonRepository) : PersonServiceGrpc.PersonServiceImplBase() {
    private val logger = KotlinLogging.logger {}

    override fun findPerson(request: PersonRequest, responseObserver: StreamObserver<PersonResponse>?) {
        runCatching {
            logger.info { "got a request" }
            val person = repository.findPersonByCpfOrNull(request.cpf)
                ?: throw PersonNotFoundException(request.cpf)

            val personResponse = PersonResponse.newBuilder()
                .setName(person.name)
                .setEmail(person.email)
                .setCpf(person.cpf)
                .setId(person.id!!)
                .addAllAddress(person.addresses.map {
                    AddressResponse.newBuilder()
                        .setId(it.id!!)
                        .setStreet(it.street).setCity(it.city).setType(it.type.name).build()
                })
                .build()

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

    override fun createPerson(request: CreatePersonRequest, responseObserver: StreamObserver<PersonResponse>?) {
        runCatching {
            if (!isValid(request)) throw InvalidRequestException()
            if (repository.personExists(request.cpf)) throw PersonAlreadyExistsException(request.cpf)

            logger.info { "creating a new person" }
            val person = request.toEntity()
            val personCreated = repository.addPerson(person)
                ?: throw PersonCreationException(person)

            val personResponse = PersonResponse.newBuilder()
                .setName(personCreated.name)
                .setEmail(personCreated.email)
                .setCpf(personCreated.cpf)
                .setId(personCreated.id!!)
                .addAllAddress(personCreated.addresses.map {
                    AddressResponse.newBuilder()
                        .setId(it.id!!) // TODO how to deal with ids?
                        .setStreet(it.street)
                        .setCity(it.city)
                        .setType(it.type.name)
                        .build()
                })
                .build()


            logger.info { "sending response: $personResponse" }
            responseObserver?.onNext(personResponse)
            responseObserver?.onCompleted()
        }.onFailure {
            logger.error { "got an exception: $it" }
            val status = when (it) {
                is InvalidRequestException -> INVALID_ARGUMENT
                is PersonAlreadyExistsException -> ALREADY_EXISTS
                else -> INTERNAL
            }

            val errorResponse = ErrorResponse.newBuilder()
                .setMessage(it.message)
                .setException(it::class.java.name)
                .build()

            responseObserver?.onError(
                status.withDescription(errorResponse.message).asException()
            )
        }
    }

    fun isValid(request: CreatePersonRequest) = with(request) {
        cpf.isNotEmpty() && email.isNotEmpty() && name.isNotEmpty()
    }
}

fun AddressRequest.toEntity() = Address(
    id = null,
    street = this.street,
    city = this.city,
    type = Address.Type.valueOf(this.type)
)

fun CreatePersonRequest.toEntity() = Person(
    id = null,
    name = this.name,
    cpf = this.cpf,
    email = this.email,
    addresses = this.addressList.map(AddressRequest::toEntity)
)
