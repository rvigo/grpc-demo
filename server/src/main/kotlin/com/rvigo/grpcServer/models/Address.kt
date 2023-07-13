package com.rvigo.grpcServer.models

data class Address(
    val id: Int? = null,
    val street: String,
    val city: String,
    val type: Type
) {
    enum class Type { HOME, WORK }
}
