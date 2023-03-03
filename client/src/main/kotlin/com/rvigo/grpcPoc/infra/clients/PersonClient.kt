package com.rvigo.grpcPoc.infra.clients

import com.rvigo.grpcPoc.adapters.grpc.person.Person.*
import com.rvigo.grpcPoc.adapters.grpc.person.PersonServiceGrpc
import com.rvigo.grpcPoc.adapters.grpc.person.PersonServiceGrpc.PersonServiceBlockingStub
import com.rvigo.grpcPoc.dtos.AddressDTO
import com.rvigo.grpcPoc.dtos.PersonDTO
import com.rvigo.grpcPoc.models.Address
import com.rvigo.grpcPoc.models.Person
import io.grpc.ManagedChannel
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class PersonClient(managedChannel: ManagedChannel) {
    private val stub: PersonServiceBlockingStub = PersonServiceGrpc.newBlockingStub(managedChannel)

    private val logger = KotlinLogging.logger {}

    fun findPersonByCpf(cpf: String): PersonDTO {
        logger.info { "searching for person with cpf \"$cpf\"" }
        val request = PersonRequest.newBuilder().setCpf(cpf).build()
        val response = stub.findPerson(request)

        val person = response.toDomain()

        return PersonDTO.fromDomain(person=person)
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

fun AddressDTO.toGrpcRequest() : AddressRequest =
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
