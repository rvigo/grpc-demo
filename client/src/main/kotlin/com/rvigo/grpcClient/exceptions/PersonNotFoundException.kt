package com.rvigo.grpcClient.exceptions

class PersonNotFoundException(cpf: String) : RuntimeException("Cannot find person with cpf $cpf")
