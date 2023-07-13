package com.rvigo.grpcClient.models

data class Person(
    val id: Int? = null,
    val name: String,
    val email: String,
    val cpf: String,
    val addresses: List<Address>
)
