package com.rvigo.grpcPoc.infra.entities

import com.rvigo.grpcPoc.infra.entities.AddressEntity.AddressTable
import com.rvigo.grpcPoc.models.Person
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

class PersonEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PersonEntity>(PersonsTable)

    object PersonsTable : IntIdTable() {
        val name = varchar("name", 50).index()
        val email = varchar("email", 100)
        val cpf = varchar("cpf", 14)
    }

    var name by PersonsTable.name
    var email by PersonsTable.email
    var cpf by PersonsTable.cpf
    val addresses by AddressEntity referrersOn AddressTable.person
}

fun PersonEntity.toDomain() =
    Person(id = id.value, name = name, email = email, cpf = cpf, addresses = addresses.map(AddressEntity::toDomain))
