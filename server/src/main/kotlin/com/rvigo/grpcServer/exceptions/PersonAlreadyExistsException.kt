package com.rvigo.grpcServer.exceptions

class PersonAlreadyExistsException(cpf: String) : RuntimeException("person with cpf $cpf already exists")
