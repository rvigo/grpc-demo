package com.rvigo.grpcClient.infra.clients

import com.rvigo.grpcClient.adapters.grpc.person.Person.AddressRequest
import com.rvigo.grpcClient.adapters.grpc.person.Person.AddressResponse
import com.rvigo.grpcClient.adapters.grpc.person.Person.CreatePersonRequest
import com.rvigo.grpcClient.adapters.grpc.person.Person.PersonRequest
import com.rvigo.grpcClient.adapters.grpc.person.Person.PersonResponse
import com.rvigo.grpcClient.adapters.grpc.person.PersonServiceGrpc
import com.rvigo.grpcClient.dtos.AddressDTO
import com.rvigo.grpcClient.dtos.PersonDTO
import com.rvigo.grpcClient.models.Address
import com.rvigo.grpcClient.models.Person
import io.grpc.ManagedChannel
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class PersonClient(managedChannel: ManagedChannel) {
    private val stub: PersonServiceGrpc.PersonServiceBlockingStub = PersonServiceGrpc.newBlockingStub(managedChannel)

    private val logger = KotlinLogging.logger {}

    fun findPersonByCpf(cpf: String): PersonDTO {
        logger.info { "searching for person with cpf \"$cpf\"" }
        val request = PersonRequest.newBuilder().setCpf(cpf).build()
        val response = stub.findPerson(request)

        val person = response.toDomain()

        return PersonDTO.fromDomain(person = person)
    }

    fun createPerson(personDto: PersonDTO): PersonDTO {
        logger.info { "creating person $personDto" }
        val createPersonRequest = CreatePersonRequest.newBuilder()
            .setName(personDto.name).setCpf(personDto.cpf).setEmail(personDto.email)
            .addAllAddress(personDto.addresses.map(AddressDTO::toGrpcRequest)).build()

        val newPerson = stub.createPerson(createPersonRequest).toDomain()

        return PersonDTO.fromDomain(person = newPerson)
    }
}

fun AddressDTO.toGrpcRequest(): AddressRequest =
    AddressRequest.newBuilder().setCity(this.city).setStreet(this.street).setType(this.type.name).build()

fun PersonResponse.toDomain() = Person(
    id = this.id,
    name = this.name,
    cpf = this.cpf,
    email = this.email,
    addresses = this.addressList.map(AddressResponse::toDomain)
)

fun AddressResponse.toDomain() = Address(
    id = this.id,
    street = this.street,
    city = this.city,
    type = Address.Type.valueOf(this.type)
)
