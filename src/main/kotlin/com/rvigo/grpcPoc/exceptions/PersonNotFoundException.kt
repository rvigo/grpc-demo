package com.rvigo.grpcPoc.exceptions

class PersonNotFoundException(private val cpf: String) : RuntimeException("Cannot find person with cpf $cpf")
