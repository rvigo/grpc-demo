package com.rvigo.grpcPoc.dtos

import com.rvigo.grpcPoc.models.Address

data class AddressDTO(
    val street: String,
    val city: String,
    val type: Type
) {
    enum class Type { HOME, WORK }

    companion object {
        fun fromDomain(address: Address) = AddressDTO(
            address.street,
            address.city,
            AddressDTO.Type.valueOf(address.type.name)
        )
    }
}

