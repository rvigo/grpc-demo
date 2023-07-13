package com.rvigo.grpcClient.controllers

import com.rvigo.grpcClient.dtos.PersonDTO
import com.rvigo.grpcClient.infra.clients.PersonClient
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("person", produces = ["application/json"], consumes = ["application/json"])
class PersonController(val personClient: PersonClient) {
    @GetMapping("{cpf}")
    fun getPerson(@PathVariable cpf: String) = personClient.findPersonByCpf(cpf)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createPerson(@RequestBody person: PersonDTO) = personClient.createPerson(person)
}
