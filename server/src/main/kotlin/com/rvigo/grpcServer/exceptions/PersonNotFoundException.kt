package com.rvigo.grpcServer.exceptions

class PersonNotFoundException(cpf: String) : RuntimeException("Cannot find person with cpf $cpf")
