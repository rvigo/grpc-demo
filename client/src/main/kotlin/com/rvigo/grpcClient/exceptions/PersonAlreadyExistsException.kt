package com.rvigo.grpcClient.exceptions

class PersonAlreadyExistsException(cpf: String) : RuntimeException("person with cpf $cpf already exists")
