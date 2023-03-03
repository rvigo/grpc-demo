package com.rvigo.grpcPoc.dtos

import com.rvigo.grpcPoc.models.Person

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
            person.addresses.map(AddressDTO::fromDomain)
        )
    }
}

