package com.rvigo.grpcPoc.exceptions

class PersonNotFoundException(cpf: String) : RuntimeException("Cannot find person with cpf $cpf")
