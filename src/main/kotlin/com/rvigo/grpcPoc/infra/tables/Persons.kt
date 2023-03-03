package com.rvigo.grpcPoc.infra.tables

import com.rvigo.grpcPoc.adapters.grpc.person.Person.CreatePersonRequest
import com.rvigo.grpcPoc.models.Person
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Persons : Table() {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 50).index()
    val email = varchar("email", 100)
    val cpf = varchar("cpf", 14)
}

fun Persons.fromRow(row: ResultRow) = Person(id = row[id], name = row[name], email = row[email], cpf = row[cpf])

fun CreatePersonRequest.toEntity() = Person(name = this.name, cpf = this.cpf, email = this.email)
