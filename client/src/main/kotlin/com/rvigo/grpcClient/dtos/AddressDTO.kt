package com.rvigo.grpcClient.dtos

import com.rvigo.grpcClient.models.Address

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
            Type.valueOf(address.type.name)
        )
    }
}

