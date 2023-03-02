package com.rvigo.grpcPoc.models

import com.rvigo.grpcPoc.infra.tables.Persons
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Person(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<Person>(Persons)

    var name by Persons.name
    var email by Persons.email
    var cpf by Persons.cpf
}
