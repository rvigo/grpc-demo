package com.rvigo.grpcPoc.exceptions

class PersonAlreadyExistsException(cpf: String) : RuntimeException("person with cpf $cpf already exists")
