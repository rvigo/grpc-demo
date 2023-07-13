package com.rvigo.grpcClient.dtos

import com.rvigo.grpcClient.models.Person

data class PersonDTO(
    val name: String,
    val email: String,
    val cpf: String,
    val addresses: List<AddressDTO>
) {
    companion object {
        fun fromDomain(person: Person) = PersonDTO(
            person.name,
            person.email,
            person.cpf,
            person.addresses.map(AddressDTO.Companion::fromDomain)
        )
    }
}

